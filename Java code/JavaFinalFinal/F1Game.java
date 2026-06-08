import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.util.Map;

public class F1Game {
    private static final int WIDTH = 720;
    private static final int HEIGHT = 480;
    private static final int VIEW_WIDTH = 460;
    private static final int VIEW_HEIGHT = 320;
    private static final int VIEW_X = (WIDTH - VIEW_WIDTH) / 2;
    private static final int VIEW_Y = (HEIGHT - VIEW_HEIGHT) / 2;

    private static final double FRICTION = 0.96;
    private static final double MAX_SPEED = 10.0;
    private static final double ACCELERATION = 0.5;
    private static final double BRAKE_DECELERATION = 0.7;
    private static final double TURN_SPEED = 0.08; // base turn speed (lower)
    private static final double TRACTION = 0.95;
    private static final double TRACK_WIDTH = 90;
    private static final double HALF_TRACK_WIDTH = TRACK_WIDTH / 2.0;
    private static final double TRACK_BOUNDARY_SCALE = 1.06;
    private static final double TRACK_SCALE = 3.6; // increased so car appears smaller relative to track (approx F1 scale)
    private static final int TRACK_DETAIL = 960;
    private static final long BEST_LAP_BLINK_DURATION = 2000;
    private static final long BEST_LAP_BLINK_INTERVAL = 250;

    private final Car car;
    private double prevCarX, prevCarY;
    private boolean lapTimingStarted = false;
    private long lapStartTime = 0;
    private long bestLapTime = Long.MAX_VALUE;
    private long bestLapBlinkStart = 0;
    private double[][] trackPoints;
    private double[][] leftBoundary;
    private double[][] rightBoundary;
    private static final int MENU_COLUMNS = 3;
    private static final int MENU_PREVIEW_WIDTH = 220;
    private static final int MENU_PREVIEW_HEIGHT = 120;
    private static final int MENU_MARGIN = 20;
    private static final int MENU_TOP_OFFSET = 100;
    private static final int EXIT_BUTTON_WIDTH = 110;
    private static final int EXIT_BUTTON_HEIGHT = 40;
    private static final int EXIT_BUTTON_MARGIN = 20;

    private String trackName;
    private int startLineDesiredSide;
    private double worldMinX, worldMinY, worldMaxX, worldMaxY;

    private TrackDefinition[] tracks;
    private Map<String, Long> bestLapTimes;
    private boolean inMenu = true;
    private boolean mouseWasPressed = false;
    private int hoveredTrackIndex = -1;
    // Transition animation state
    private boolean transitioning = false;
    private int transitionDirection = 0; // 1: menu->game, -1: game->menu
    private double transitionProgress = 0.0; // 0..1
    private static final double TRANSITION_DURATION = 0.5; // seconds

    private static final int AUDIO_SAMPLE_RATE = 44100;
    private static final double ACCEL_SOUND_DURATION = 0.18;
    private static final double BRAKE_SOUND_DURATION = 0.18;
    private static final long AUDIO_THROTTLE_MS = 120;
    private long lastAccelSoundTime = 0;
    private long lastBrakeSoundTime = 0;

    public F1Game() {
        car = new Car(WIDTH / 2, HEIGHT / 2);
        initializeTracks();
    }

    private void initializeTracks() {
        this.tracks = new TrackDefinition[]{
            new TrackDefinition("Monaco", new double[][]{
                {520, 350}, {480, 410}, {420, 450}, {360, 470}, {300, 450},
                {250, 390}, {240, 320}, {260, 250}, {310, 210}, {380, 190},
                {460, 190}, {520, 220}, {560, 280}, {590, 340}, {570, 380},
                {520, 350}
            }),
            new TrackDefinition("Silverstone", new double[][]{
                {180, 100}, {500, 80}, {840, 140}, {920, 260}, {850, 380},
                {720, 430}, {580, 520}, {370, 520}, {220, 420}, {170, 300},
                {190, 210}, {250, 160}, {340, 130}, {420, 120}
            }),
            new TrackDefinition("Long Circuit", new double[][]{
                {200, 150}, {500, 140}, {800, 160}, {1000, 300}, {900, 500},
                {600, 550}, {350, 500}, {200, 350}, {220, 200}
            }),
            new TrackDefinition("Monza", new double[][]{
                {220, 220}, {420, 160}, {660, 180}, {840, 260}, {820, 360},
                {640, 460}, {460, 500}, {300, 420}, {220, 300}, {220, 220}
            }),
            new TrackDefinition("Suzuka", new double[][]{
                {160, 260}, {260, 110}, {380, 100}, {520, 140}, {620, 240},
                {650, 330}, {620, 430}, {520, 520}, {410, 520}, {310, 450},
                {270, 360}, {290, 270}, {340, 220}, {430, 200}, {540, 240},
                {620, 330}
            }),
            new TrackDefinition("Spa", new double[][]{
                {270, 160}, {460, 120}, {690, 160}, {830, 300}, {760, 470},
                {620, 560}, {430, 560}, {260, 470}, {190, 340}, {220, 220}
            })
        };

        int selectedIndex = (int) (Math.random() * this.tracks.length);
        TrackDefinition selected = this.tracks[selectedIndex];
        trackName = selected.name;
        trackPoints = generateTrack(prepareAnchors(selected.anchors), TRACK_DETAIL);
        generateTrackBoundaries();
        updateWorldBounds();

        double[][] startLine = getStartFinishLineEndpoints();
        startLineDesiredSide = sign(direction(startLine[0][0], startLine[0][1],
                startLine[1][0], startLine[1][1], trackPoints[1][0], trackPoints[1][1]));
        if (startLineDesiredSide == 0) {
            startLineDesiredSide = 1;
        }

        car.setPosition(trackPoints[0][0], trackPoints[0][1]);
        car.setAngle(Math.atan2(trackPoints[1][1] - trackPoints[0][1], trackPoints[1][0] - trackPoints[0][0]));
        prevCarX = car.getX();
        prevCarY = car.getY();
    }

    private double[][] generateTrack(double[][] anchors, int numPoints) {
        // Use Catmull-Rom spline for smoother track
        int anchorCount = anchors.length;
        double[][] pts = new double[numPoints][2];
        for (int i = 0; i < numPoints; i++) {
            double t = (double) i / numPoints * anchorCount;
            int idx = (int) Math.floor(t) % anchorCount;
            double local = t - Math.floor(t);

            // get control points p0,p1,p2,p3 (wrap)
            double[] p1 = anchors[idx];
            double[] p0 = anchors[(idx - 1 + anchorCount) % anchorCount];
            double[] p2 = anchors[(idx + 1) % anchorCount];
            double[] p3 = anchors[(idx + 2) % anchorCount];

            // Catmull-Rom basis
            double t2 = local * local;
            double t3 = t2 * local;

            double x = 0.5 * ((2 * p1[0]) + (-p0[0] + p2[0]) * local + (2 * p0[0] - 5 * p1[0] + 4 * p2[0] - p3[0]) * t2 + (-p0[0] + 3 * p1[0] - 3 * p2[0] + p3[0]) * t3);
            double y = 0.5 * ((2 * p1[1]) + (-p0[1] + p2[1]) * local + (2 * p0[1] - 5 * p1[1] + 4 * p2[1] - p3[1]) * t2 + (-p0[1] + 3 * p1[1] - 3 * p2[1] + p3[1]) * t3);

            pts[i][0] = x * TRACK_SCALE;
            pts[i][1] = y * TRACK_SCALE;
        }

        return pts;
    }

    private double[][] prepareAnchors(double[][] anchors) {
        if (anchors.length > 1 && anchors[0][0] == anchors[anchors.length - 1][0]
                && anchors[0][1] == anchors[anchors.length - 1][1]) {
            double[][] cleaned = new double[anchors.length - 1][2];
            for (int i = 0; i < anchors.length - 1; i++) {
                cleaned[i][0] = anchors[i][0];
                cleaned[i][1] = anchors[i][1];
            }
            return cleaned;
        }
        return anchors;
    }

    private void updateWorldBounds() {
        worldMinX = Double.POSITIVE_INFINITY;
        worldMinY = Double.POSITIVE_INFINITY;
        worldMaxX = Double.NEGATIVE_INFINITY;
        worldMaxY = Double.NEGATIVE_INFINITY;
        for (double[] point : trackPoints) {
            worldMinX = Math.min(worldMinX, point[0]);
            worldMinY = Math.min(worldMinY, point[1]);
            worldMaxX = Math.max(worldMaxX, point[0]);
            worldMaxY = Math.max(worldMaxY, point[1]);
        }
        double padding = TRACK_WIDTH + 120;
        worldMinX -= padding;
        worldMinY -= padding;
        worldMaxX += padding;
        worldMaxY += padding;
        car.setWorldBounds(worldMinX, worldMinY, worldMaxX, worldMaxY);
    }

    private void generateTrackBoundaries() {
        int n = trackPoints.length;
        leftBoundary = new double[n][2];
        rightBoundary = new double[n][2];
        for (int i = 0; i < n; i++) {
            int next = (i + 1) % n;
            int prev = (i - 1 + n) % n;
            double dx1 = trackPoints[i][0] - trackPoints[prev][0];
            double dy1 = trackPoints[i][1] - trackPoints[prev][1];
            double dx2 = trackPoints[next][0] - trackPoints[i][0];
            double dy2 = trackPoints[next][1] - trackPoints[i][1];
            double dx = dx1 + dx2;
            double dy = dy1 + dy2;
            if (Math.hypot(dx, dy) < 1e-6) {
                dx = trackPoints[next][0] - trackPoints[prev][0];
                dy = trackPoints[next][1] - trackPoints[prev][1];
            }
            double len = Math.hypot(dx, dy);
            if (len == 0) {
                len = 1;
            }
            double nx = -dy / len;
            double ny = dx / len;
            leftBoundary[i][0] = trackPoints[i][0] + nx * HALF_TRACK_WIDTH * TRACK_BOUNDARY_SCALE;
            leftBoundary[i][1] = trackPoints[i][1] + ny * HALF_TRACK_WIDTH * TRACK_BOUNDARY_SCALE;
            rightBoundary[i][0] = trackPoints[i][0] - nx * HALF_TRACK_WIDTH * TRACK_BOUNDARY_SCALE;
            rightBoundary[i][1] = trackPoints[i][1] - ny * HALF_TRACK_WIDTH * TRACK_BOUNDARY_SCALE;
        }
    }

    private double[][] getStartFinishLineEndpoints() {
        double x1 = trackPoints[0][0];
        double y1 = trackPoints[0][1];
        double x2 = trackPoints[1][0];
        double y2 = trackPoints[1][1];
        double dx = x2 - x1;
        double dy = y2 - y1;
        double len = Math.hypot(dx, dy);
        if (len == 0) {
            len = 1;
        }
        double nx = -dy / len;
        double ny = dx / len;
        double halfWidth = HALF_TRACK_WIDTH * TRACK_BOUNDARY_SCALE * 1.2;
        return new double[][]{
            {x1 + nx * halfWidth, y1 + ny * halfWidth},
            {x1 - nx * halfWidth, y1 - ny * halfWidth}
        };
    }

    private void checkTrackCollision() {
        double px = car.getX();
        double py = car.getY();
        double closest = Double.MAX_VALUE;
        double[] nearestPoint = null;
        for (int i = 0; i < trackPoints.length; i++) {
            int next = (i + 1) % trackPoints.length;
            double[] projected = projectPointOnSegment(px, py, trackPoints[i], trackPoints[next]);
            double dist = Math.hypot(px - projected[0], py - projected[1]);
            if (dist < closest) {
                closest = dist;
                nearestPoint = projected;
            }
        }
        if (closest > HALF_TRACK_WIDTH) {
            car.crash();
        }
    }

    // Check crossing of start/finish line (between trackPoints[0] and trackPoints[1])
    private void checkStartFinishCrossing() {
        double x1 = prevCarX;
        double y1 = prevCarY;
        double x2 = car.getX();
        double y2 = car.getY();

        double[][] startLine = getStartFinishLineEndpoints();
        double sx1 = startLine[0][0];
        double sy1 = startLine[0][1];
        double sx2 = startLine[1][0];
        double sy2 = startLine[1][1];

        double prevSide = direction(sx1, sy1, sx2, sy2, x1, y1);
        double currSide = direction(sx1, sy1, sx2, sy2, x2, y2);

        if (segmentsIntersect(x1, y1, x2, y2, sx1, sy1, sx2, sy2) && currSide != 0) {
            int crossingIntoSide = sign(currSide);
            if (crossingIntoSide == startLineDesiredSide) {
                long now = System.currentTimeMillis();
                if (!lapTimingStarted) {
                    lapTimingStarted = true;
                    lapStartTime = now;
                } else {
                    long lapTime = now - lapStartTime;
                    if (lapTime > 500) {
                        if (lapTime < bestLapTime) {
                            bestLapTime = lapTime;
                            bestLapBlinkStart = now;
                        }
                        lapStartTime = now;
                    }
                }
            }
        }

        prevCarX = x2;
        prevCarY = y2;
    }

    private boolean segmentsIntersect(double x1, double y1, double x2, double y2,
                                      double x3, double y3, double x4, double y4) {
        double d1 = direction(x3, y3, x4, y4, x1, y1);
        double d2 = direction(x3, y3, x4, y4, x2, y2);
        double d3 = direction(x1, y1, x2, y2, x3, y3);
        double d4 = direction(x1, y1, x2, y2, x4, y4);
        return d1 * d2 <= 0 && d3 * d4 <= 0;
    }

    private int sign(double value) {
        return value > 0 ? 1 : (value < 0 ? -1 : 0);
    }

    private double direction(double ax, double ay, double bx, double by, double cx, double cy) {
        return (cx - ax) * (by - ay) - (cy - ay) * (bx - ax);
    }

    private double pointSegmentDistance(double px, double py, double[] a, double[] b) {
        double ax = a[0];
        double ay = a[1];
        double bx = b[0];
        double by = b[1];
        double dx = bx - ax;
        double dy = by - ay;
        if (dx == 0 && dy == 0) {
            return Math.hypot(px - ax, py - ay);
        }
        double t = ((px - ax) * dx + (py - ay) * dy) / (dx * dx + dy * dy);
        t = Math.max(0, Math.min(1, t));
        double projX = ax + t * dx;
        double projY = ay + t * dy;
        return Math.hypot(px - projX, py - projY);
    }

    private double[] projectPointOnSegment(double px, double py, double[] a, double[] b) {
        double ax = a[0];
        double ay = a[1];
        double bx = b[0];
        double by = b[1];
        double dx = bx - ax;
        double dy = by - ay;
        if (dx == 0 && dy == 0) {
            return new double[]{ax, ay};
        }
        double t = ((px - ax) * dx + (py - ay) * dy) / (dx * dx + dy * dy);
        t = Math.max(0, Math.min(1, t));
        return new double[]{ax + t * dx, ay + t * dy};
    }

    public void run() {
        StdDraw.setCanvasSize(WIDTH, HEIGHT);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.enableDoubleBuffering();

        long last = System.nanoTime();
        while (true) {
            long now = System.nanoTime();
            double dt = (now - last) / 1e9;
            last = now;

            handleInput();

            // update transition progress if animating
            if (transitioning) {
                transitionProgress += dt / TRANSITION_DURATION;
                if (transitionProgress >= 1.0) {
                    // finalize
                    transitionProgress = 0.0;
                    transitioning = false;
                    if (transitionDirection == 1) {
                        inMenu = false;
                    } else if (transitionDirection == -1) {
                        inMenu = true;
                    }
                    transitionDirection = 0;
                } else {
                    // at halfway swap the scene
                    if (transitionDirection == 1 && transitionProgress >= 0.5 && inMenu) {
                        inMenu = false;
                    }
                    if (transitionDirection == -1 && transitionProgress >= 0.5 && !inMenu) {
                        inMenu = true;
                    }
                }
            } else {
                // normal updates
                if (!inMenu) {
                    car.update(FRICTION, MAX_SPEED, TRACTION);
                    checkTrackCollision();
                    checkStartFinishCrossing();
                }
            }

            draw();
            StdDraw.pause(16);
        }
    }

    private void handleInput() {
        if (inMenu) {
            handleMenuInput();
        } else {
            handleTrackInput();
        }
    }

    private void handleTrackInput() {
        boolean acceleratingNow = StdDraw.isKeyPressed('W') || StdDraw.isKeyPressed('w') || StdDraw.isKeyPressed(KeyEvent.VK_UP);
        boolean brakingNow = StdDraw.isKeyPressed('S') || StdDraw.isKeyPressed('s') || StdDraw.isKeyPressed(KeyEvent.VK_DOWN);

        if (acceleratingNow) {
            car.accelerate(ACCELERATION);
            playAccelerationSound();
        }
        if (brakingNow) {
            car.brake(BRAKE_DECELERATION);
            playBrakeSound();
        }
        // Arrow keys (left/right)
        if (StdDraw.isKeyPressed('A') || StdDraw.isKeyPressed('a')) {
            car.turnLeft(TURN_SPEED);
        }
        if (StdDraw.isKeyPressed('D') || StdDraw.isKeyPressed('d')) {
            car.turnRight(TURN_SPEED);
        }
        if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT)) {
            car.turnLeft(TURN_SPEED);
        }
        if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT)) {
            car.turnRight(TURN_SPEED);
        }

        // Check exit button click
        if (StdDraw.isMousePressed()) {
            if (!mouseWasPressed) {
                double mouseX = StdDraw.mouseX();
                double mouseY = StdDraw.mouseY();

                if (mouseX >= WIDTH - EXIT_BUTTON_MARGIN - EXIT_BUTTON_WIDTH && 
                    mouseX <= WIDTH - EXIT_BUTTON_MARGIN &&
                    mouseY >= EXIT_BUTTON_MARGIN && 
                    mouseY <= EXIT_BUTTON_MARGIN + EXIT_BUTTON_HEIGHT) {
                    inMenu = true;
                    mouseWasPressed = true;
                    return;
                }
            }
            mouseWasPressed = true;
        } else {
            mouseWasPressed = false;
        }
    }

    private void handleMenuInput() {
        if (StdDraw.isMousePressed()) {
            if (!mouseWasPressed) {
                double mouseX = StdDraw.mouseX();
                double mouseY = StdDraw.mouseY();

                // Check track selection
                for (int i = 0; i < tracks.length; i++) {
                    if (isMouseInTrackButton(i, mouseX, mouseY)) {
                        selectTrack(i);
                        inMenu = false;
                        lapTimingStarted = false;
                        lapStartTime = 0;
                        mouseWasPressed = true;
                        return;
                    }
                }
            }
            mouseWasPressed = true;
        } else {
            mouseWasPressed = false;
        }
    }

    private void playAccelerationSound() {
        long now = System.currentTimeMillis();
        if (now - lastAccelSoundTime < AUDIO_THROTTLE_MS) {
            return;
        }
        lastAccelSoundTime = now;
        new Thread(() -> StdAudio.play(createAccelerationSound())).start();
    }

    private void playBrakeSound() {
        long now = System.currentTimeMillis();
        if (now - lastBrakeSoundTime < AUDIO_THROTTLE_MS) {
            return;
        }
        lastBrakeSoundTime = now;
        new Thread(() -> StdAudio.play(createBrakeSound())).start();
    }

    private double[] createAccelerationSound() {
        int length = (int) (AUDIO_SAMPLE_RATE * ACCEL_SOUND_DURATION);
        double[] samples = new double[length];
        double phase = 0.0;
        for (int i = 0; i < length; i++) {
            double t = length > 1 ? (double) i / (length - 1) : 0.0;
            t = Math.min(t, 1.0); // Clamp to ensure it never exceeds 1.0
            double freq = 180 + 400 * t;
            phase += 2 * Math.PI * freq / AUDIO_SAMPLE_RATE;
            double envelope = t < 0.1 ? t * 10 : 1.0 - 0.5 * (t - 0.1);
            double tone = Math.sin(phase);
            double noise = (Math.random() * 2 - 1) * 0.08 * (1 - t);
            double sample = 0.35 * tone * envelope + noise;
            samples[i] = Math.max(-1.0, Math.min(1.0, sample));
        }
        return samples;
    }

    private double[] createBrakeSound() {
        int length = (int) (AUDIO_SAMPLE_RATE * BRAKE_SOUND_DURATION);
        double[] samples = new double[length];
        double phase = 0.0;
        for (int i = 0; i < length; i++) {
            double t = length > 1 ? (double) i / (length - 1) : 0.0;
            t = Math.min(t, 1.0); // Clamp to ensure it never exceeds 1.0
            double envelope = 1.0 - t;
            double noise = (Math.random() * 2 - 1) * 0.25 * envelope;
            phase += 2 * Math.PI * 80 / AUDIO_SAMPLE_RATE;
            double lowTone = Math.sin(phase) * 0.18 * envelope;
            double sample = (noise + lowTone) * 0.7;
            samples[i] = Math.max(-1.0, Math.min(1.0, sample));
        }
        return samples;
    }

    private boolean isMouseInTrackButton(int trackIndex, double mouseX, double mouseY) {
        int col = trackIndex % MENU_COLUMNS;
        int row = trackIndex / MENU_COLUMNS;
        int x = MENU_MARGIN + col * (MENU_PREVIEW_WIDTH + MENU_MARGIN);
        int y = MENU_TOP_OFFSET + row * (MENU_PREVIEW_HEIGHT + MENU_MARGIN);
        return mouseX >= x && mouseX <= x + MENU_PREVIEW_WIDTH &&
               mouseY >= y && mouseY <= y + MENU_PREVIEW_HEIGHT;
    }

    private void selectTrack(int index) {
        TrackDefinition selected = tracks[index];
        trackName = selected.name;
        trackPoints = generateTrack(prepareAnchors(selected.anchors), TRACK_DETAIL);
        generateTrackBoundaries();
        updateWorldBounds();

        double[][] startLine = getStartFinishLineEndpoints();
        startLineDesiredSide = sign(direction(startLine[0][0], startLine[0][1],
                startLine[1][0], startLine[1][1], trackPoints[1][0], trackPoints[1][1]));
        if (startLineDesiredSide == 0) {
            startLineDesiredSide = 1;
        }

        car.setPosition(trackPoints[0][0], trackPoints[0][1]);
        car.setAngle(Math.atan2(trackPoints[1][1] - trackPoints[0][1], trackPoints[1][0] - trackPoints[0][0]));
        prevCarX = car.getX();
        prevCarY = car.getY();
    }

    private void draw() {
        StdDraw.clear(StdDraw.DARK_GRAY);

        if (inMenu) {
            drawMenu();
        } else {
            double camX = car.getX();
            double camY = car.getY();

            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.filledRectangle(VIEW_X + VIEW_WIDTH / 2.0, VIEW_Y + VIEW_HEIGHT / 2.0,
                    VIEW_WIDTH / 2.0, VIEW_HEIGHT / 2.0);

            drawGrassTexture(camX, camY);
            drawTrackSurface(camX, camY);

            StdDraw.setPenRadius(0.004);
            StdDraw.setPenColor(StdDraw.RED);
            for (int i = 0; i < leftBoundary.length; i++) {
                int next = (i + 1) % leftBoundary.length;
                double x1 = worldToScreenX(leftBoundary[i][0], camX);
                double y1 = worldToScreenY(leftBoundary[i][1], camY);
                double x2 = worldToScreenX(leftBoundary[next][0], camX);
                double y2 = worldToScreenY(leftBoundary[next][1], camY);
                StdDraw.line(x1, y1, x2, y2);
                x1 = worldToScreenX(rightBoundary[i][0], camX);
                y1 = worldToScreenY(rightBoundary[i][1], camY);
                x2 = worldToScreenX(rightBoundary[next][0], camX);
                y2 = worldToScreenY(rightBoundary[next][1], camY);
                StdDraw.line(x1, y1, x2, y2);
            }

            StdDraw.setPenColor(StdDraw.YELLOW);
            StdDraw.setPenRadius(0.008);
            for (int i = 0; i < trackPoints.length; i++) {
                int next = (i + 1) % trackPoints.length;
                double x1 = worldToScreenX(trackPoints[i][0], camX);
                double y1 = worldToScreenY(trackPoints[i][1], camY);
                double x2 = worldToScreenX(trackPoints[next][0], camX);
                double y2 = worldToScreenY(trackPoints[next][1], camY);
                StdDraw.line(x1, y1, x2, y2);
            }

            // Draw start/finish line across the track width at the first track point
            double[][] startLine = getStartFinishLineEndpoints();
            drawCheckerboardLine(startLine[0][0], startLine[0][1], startLine[1][0], startLine[1][1], 12, camX, camY);

            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.setPenRadius(0.005);
            StdDraw.rectangle(VIEW_X + VIEW_WIDTH / 2.0, VIEW_Y + VIEW_HEIGHT / 2.0,
                    VIEW_WIDTH / 2.0, VIEW_HEIGHT / 2.0);

            car.draw(camX, camY, VIEW_X, VIEW_Y, VIEW_WIDTH, VIEW_HEIGHT);
            drawHUD();
            drawExitButton();
        }
        StdDraw.show();
    }

    private void drawMenu() {
        // Gradient background
        for (int i = 0; i < 200; i++) {
            int t = (int) (150 + (i * 0.5));
            StdDraw.setPenColor(new Color(t, t, 255));
            double h = HEIGHT * (i / 200.0);
            StdDraw.filledRectangle(WIDTH / 2.0, h + HEIGHT / 400.0, WIDTH / 2.0, HEIGHT / 400.0);
        }

        // Top card
        double cardW = WIDTH - 80;
        double cardH = 120;
        double cardX = WIDTH / 2.0;
        double cardY = HEIGHT - 70;
        StdDraw.setPenColor(new Color(20, 20, 20, 220));
        StdDraw.filledRectangle(cardX, cardY, cardW / 2.0, cardH / 2.0);
        StdDraw.setPenColor(new Color(255, 255, 255, 200));
        StdDraw.setPenRadius(0.006);
        StdDraw.rectangle(cardX, cardY, cardW / 2.0, cardH / 2.0);

        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(new java.awt.Font("Verdana", java.awt.Font.BOLD, 36));
        StdDraw.text(cardX - 10, cardY + 18, "F1 Racing");
        StdDraw.setFont(new java.awt.Font("Verdana", java.awt.Font.PLAIN, 16));
        StdDraw.text(cardX - 10, cardY - 12, "Choose a track and press Start to race!");

        // Draw track buttons with previews
        for (int i = 0; i < tracks.length; i++) {
            int col = i % MENU_COLUMNS;
            int row = i / MENU_COLUMNS;
            int x = MENU_MARGIN + col * (MENU_PREVIEW_WIDTH + MENU_MARGIN);
            int y = MENU_TOP_OFFSET + row * (MENU_PREVIEW_HEIGHT + MENU_MARGIN);

            double centerX = x + MENU_PREVIEW_WIDTH / 2.0;
            double centerY = y + MENU_PREVIEW_HEIGHT / 2.0;

            // Shadow
            StdDraw.setPenColor(new Color(0, 0, 0, 100));
            StdDraw.filledRectangle(centerX + 4, centerY - 4, MENU_PREVIEW_WIDTH / 2.0, MENU_PREVIEW_HEIGHT / 2.0);

            // Button background
            if (i == hoveredTrackIndex) {
                StdDraw.setPenColor(new Color(235, 235, 180));
            } else {
                StdDraw.setPenColor(new Color(230, 230, 230));
            }
            StdDraw.filledRectangle(centerX, centerY, MENU_PREVIEW_WIDTH / 2.0, MENU_PREVIEW_HEIGHT / 2.0);

            // Track layout preview
            double previewWidth = 80;
            double previewHeight = 60;
            double previewLeft = x + 12;
            double previewBottom = y + MENU_PREVIEW_HEIGHT - previewHeight - 12;
            drawTrackLayoutPreview(tracks[i], previewLeft, previewBottom, previewWidth, previewHeight);

            // Border
            StdDraw.setPenRadius(0.003);
            StdDraw.setPenColor(new Color(30, 30, 30));
            StdDraw.rectangle(centerX, centerY, MENU_PREVIEW_WIDTH / 2.0, MENU_PREVIEW_HEIGHT / 2.0);

            // Track name
            StdDraw.setFont(new Font("Arial", Font.BOLD, 14));
            StdDraw.setPenColor(new Color(10, 10, 10));
            StdDraw.text(x + MENU_PREVIEW_WIDTH * 0.7, y + 22, tracks[i].name);
        }

        // Start button
        double startW = 160, startH = 40;
        double startX = WIDTH - 120, startY = 80;
        StdDraw.setPenColor(new Color(200, 30, 30));
        StdDraw.filledRectangle(startX, startY, startW / 2.0, startH / 2.0);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(new Font("Verdana", Font.BOLD, 18));
        StdDraw.text(startX, startY, "Start Race");

        // Footer hint
        StdDraw.setFont(new Font("Arial", Font.PLAIN, 12));
        StdDraw.setPenColor(new Color(255, 255, 255, 180));
        StdDraw.text(120, 40, "Use mouse to select a track. Press Esc to quit.");
    }

    private void drawTrackLayoutPreview(TrackDefinition track, double left, double bottom, double width, double height) {
        double[][] anchors = prepareAnchors(track.anchors);
        double minX = Double.POSITIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        for (double[] point : anchors) {
            minX = Math.min(minX, point[0]);
            minY = Math.min(minY, point[1]);
            maxX = Math.max(maxX, point[0]);
            maxY = Math.max(maxY, point[1]);
        }
        if (minX == Double.POSITIVE_INFINITY) {
            return;
        }
        double trackWidth = Math.max(maxX - minX, 1.0);
        double trackHeight = Math.max(maxY - minY, 1.0);
        double scale = Math.min((width - 12) / trackWidth, (height - 12) / trackHeight);
        double centerX = left + width / 2.0;
        double centerY = bottom + height / 2.0;
        double offsetX = centerX - (minX + maxX) / 2.0 * scale;
        double offsetY = centerY - (minY + maxY) / 2.0 * scale;

        StdDraw.setPenColor(new Color(40, 40, 40));
        StdDraw.setPenRadius(0.006);
        for (int i = 0; i < anchors.length; i++) {
            int next = (i + 1) % anchors.length;
            double x1 = offsetX + anchors[i][0] * scale;
            double y1 = offsetY + anchors[i][1] * scale;
            double x2 = offsetX + anchors[next][0] * scale;
            double y2 = offsetY + anchors[next][1] * scale;
            StdDraw.line(x1, y1, x2, y2);
        }
    }

    private double worldToScreenX(double worldX, double camX) {
        return VIEW_X + worldX - camX + VIEW_WIDTH / 2.0;
    }

    private double worldToScreenY(double worldY, double camY) {
        return VIEW_Y + worldY - camY + VIEW_HEIGHT / 2.0;
    }

    private void drawCheckerboardLine(double x1, double y1, double x2, double y2, int segments,
                                      double camX, double camY) {
        double sx1 = worldToScreenX(x1, camX);
        double sy1 = worldToScreenY(y1, camY);
        double sx2 = worldToScreenX(x2, camX);
        double sy2 = worldToScreenY(y2, camY);
        double dx = (sx2 - sx1) / segments;
        double dy = (sy2 - sy1) / segments;
        StdDraw.setPenRadius(0.012);
        for (int i = 0; i < segments; i++) {
            StdDraw.setPenColor((i % 2 == 0) ? StdDraw.WHITE : StdDraw.BLACK);
            StdDraw.line(sx1 + i * dx, sy1 + i * dy, sx1 + (i + 1) * dx, sy1 + (i + 1) * dy);
        }
    }

    private void drawTrackSurface(double camX, double camY) {
        int n = trackPoints.length;
        double[] xpts = new double[n * 2];
        double[] ypts = new double[n * 2];
        for (int i = 0; i < n; i++) {
            xpts[i] = worldToScreenX(leftBoundary[i][0], camX);
            ypts[i] = worldToScreenY(leftBoundary[i][1], camY);
        }
        for (int i = 0; i < n; i++) {
            xpts[n + i] = worldToScreenX(rightBoundary[n - 1 - i][0], camX);
            ypts[n + i] = worldToScreenY(rightBoundary[n - 1 - i][1], camY);
        }

        StdDraw.setPenColor(StdDraw.GRAY);
        StdDraw.filledPolygon(xpts, ypts);

        StdDraw.setPenRadius(0.003);
        StdDraw.setPenColor(StdDraw.DARK_GRAY);
        for (int i = 0; i < n; i += 12) {
            int next = (i + 1) % n;
            double cx1 = (leftBoundary[i][0] + rightBoundary[i][0]) / 2.0;
            double cy1 = (leftBoundary[i][1] + rightBoundary[i][1]) / 2.0;
            double cx2 = (leftBoundary[next][0] + rightBoundary[next][0]) / 2.0;
            double cy2 = (leftBoundary[next][1] + rightBoundary[next][1]) / 2.0;
            double dx = cx2 - cx1;
            double dy = cy2 - cy1;
            double len = Math.hypot(dx, dy);
            if (len == 0) {
                continue;
            }
            double nx = -dy / len;
            double ny = dx / len;
            double segmentWidth = HALF_TRACK_WIDTH * 0.8;
            double sx1 = worldToScreenX(cx1 + nx * segmentWidth, camX);
            double sy1 = worldToScreenY(cy1 + ny * segmentWidth, camY);
            double sx2 = worldToScreenX(cx1 - nx * segmentWidth, camX);
            double sy2 = worldToScreenY(cy1 - ny * segmentWidth, camY);
            StdDraw.line(sx1, sy1, sx2, sy2);
        }
    }

    private void drawGrassTexture(double camX, double camY) {
        // Fill the view area with a dark green base for the grass
        StdDraw.setPenColor(new Color(22, 120, 30));
        StdDraw.filledRectangle(VIEW_X + VIEW_WIDTH / 2.0, VIEW_Y + VIEW_HEIGHT / 2.0,
                VIEW_WIDTH / 2.0 + 10, VIEW_HEIGHT / 2.0 + 10);

        // Draw a simple grass texture over the fill
        StdDraw.setPenRadius(0.002);
        for (double x = camX - VIEW_WIDTH / 2.0 - 30; x <= camX + VIEW_WIDTH / 2.0 + 30; x += 16) {
            for (double y = camY - VIEW_HEIGHT / 2.0 - 30; y <= camY + VIEW_HEIGHT / 2.0 + 30; y += 16) {
                double sx = worldToScreenX(x, camX);
                double sy = worldToScreenY(y, camY);
                if (sx >= VIEW_X - 10 && sx <= VIEW_X + VIEW_WIDTH + 10
                        && sy >= VIEW_Y - 10 && sy <= VIEW_Y + VIEW_HEIGHT + 10) {
                    int ix = (int) Math.round(x);
                    int iy = (int) Math.round(y);
                    if (((ix + iy) & 1) == 0) {
                        StdDraw.setPenColor(new Color(45, 180, 45));
                        StdDraw.line(sx - 2, sy - 2, sx + 2, sy + 2);
                    } else {
                        StdDraw.setPenColor(new Color(35, 160, 35));
                        StdDraw.point(sx, sy);
                    }
                }
            }
        }
    }

    private void drawExitButton() {
        int exitX = WIDTH - EXIT_BUTTON_MARGIN - EXIT_BUTTON_WIDTH / 2 - 80;
        int exitY = EXIT_BUTTON_MARGIN + EXIT_BUTTON_HEIGHT / 2;
        StdDraw.setPenColor(new Color(180, 50, 50));
        StdDraw.filledRectangle(exitX + EXIT_BUTTON_WIDTH / 2.0, exitY + EXIT_BUTTON_HEIGHT / 2.0,
                EXIT_BUTTON_WIDTH / 2.0, EXIT_BUTTON_HEIGHT / 2.0);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.setPenRadius(0.003);
        StdDraw.rectangle(exitX + EXIT_BUTTON_WIDTH / 2.0, exitY + EXIT_BUTTON_HEIGHT / 2.0,
                EXIT_BUTTON_WIDTH / 2.0, EXIT_BUTTON_HEIGHT / 2.0);
        StdDraw.setFont(new Font("Arial", Font.PLAIN, 12));
        StdDraw.text(exitX + EXIT_BUTTON_WIDTH / 2.0, exitY + EXIT_BUTTON_HEIGHT / 2.0, "EXIT");
    }

    private void drawHUD() {
        long now = System.currentTimeMillis();
        boolean drawBestLap = true;
        if (bestLapBlinkStart > 0) {
            long elapsed = now - bestLapBlinkStart;
            if (elapsed < BEST_LAP_BLINK_DURATION) {
                drawBestLap = ((elapsed / BEST_LAP_BLINK_INTERVAL) % 2 == 0);
            } else {
                bestLapBlinkStart = 0;
            }
        }

        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.setFont();
        StdDraw.text(WIDTH / 2.0, HEIGHT - 30, "Track: " + trackName);
        StdDraw.text(90, HEIGHT - 30, String.format("Speed: %.1f", car.getSpeed()));
        // Lap timing display
        String currentLap = "--:--.---";
        if (lapTimingStarted) {
            currentLap = formatTime(now - lapStartTime);
        }
        String bestLap = (bestLapTime == Long.MAX_VALUE) ? "--:--.---" : formatTime(bestLapTime);
        StdDraw.text(90, HEIGHT - 70, "Lap: " + currentLap);
        if (drawBestLap) {
            StdDraw.setPenColor(new Color(255, 215, 0));
            StdDraw.text(90, HEIGHT - 90, "Best: " + bestLap);
            StdDraw.setPenColor(StdDraw.WHITE);
        }
        StdDraw.text(90, HEIGHT - 110, "W/up:Accelerate S/down:Brake A/left:Left D/right:Right");
    }

    private String formatTime(long ms) {
        long minutes = ms / 60000;
        long seconds = (ms / 1000) % 60;
        long millis = ms % 1000;
        return String.format("%02d:%02d.%03d", minutes, seconds, millis);
    }

    public static void main(String[] args) {
        F1Game game = new F1Game();
        game.run();
    }

    private static class TrackDefinition {
        public final String name;
        public final double[][] anchors;

        public TrackDefinition(String name, double[][] anchors) {
            this.name = name;
            this.anchors = anchors;
        }
    }
}

class Car {
    private double x, y;
    private double vx, vy;
    private double angle;
    private static final double WIDTH = 15;
    private static final double HEIGHT = 25;
    private static final double WORLD_WIDTH = 3200;
    private static final double WORLD_HEIGHT = 2800;
    private static final double CRASH_SLOWDOWN = 0.4;
    private double worldMinX = 0;
    private double worldMinY = 0;
    private double worldMaxX = WORLD_WIDTH;
    private double worldMaxY = WORLD_HEIGHT;

    public Car(double x, double y) {
        this.x = x;
        this.y = y;
        this.vx = 0;
        this.vy = 0;
        this.angle = 0;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void accelerate(double acceleration) {
        vx += Math.cos(angle) * acceleration;
        vy += Math.sin(angle) * acceleration;
    }

    public void brake(double deceleration) {
        double speed = Math.sqrt(vx * vx + vy * vy);
        if (speed > 0) {
            vx -= (vx / speed) * deceleration;
            vy -= (vy / speed) * deceleration;
        }
    }

    public void turnLeft(double turnAmount) {
        angle += turnAmount;
    }

    public void turnRight(double turnAmount) {
        angle -= turnAmount;
    }

    public void update(double friction, double maxSpeed, double traction) {
        vx *= friction;
        vy *= friction;

        double speed = Math.sqrt(vx * vx + vy * vy);
        if (speed > maxSpeed) {
            double scale = maxSpeed / speed;
            vx *= scale;
            vy *= scale;
        }

        double forwardVel = Math.cos(angle) * vx + Math.sin(angle) * vy;
        double sidewaysVel = -Math.sin(angle) * vx + Math.cos(angle) * vy;
        sidewaysVel *= traction;

        vx = Math.cos(angle) * forwardVel - Math.sin(angle) * sidewaysVel;
        vy = Math.sin(angle) * forwardVel + Math.cos(angle) * sidewaysVel;

        x += vx;
        y += vy;

        if (x < worldMinX) x = worldMinX;
        if (x > worldMaxX) x = worldMaxX;
        if (y < worldMinY) y = worldMinY;
        if (y > worldMaxY) y = worldMaxY;
    }

    public void setWorldBounds(double minX, double minY, double maxX, double maxY) {
        worldMinX = minX;
        worldMinY = minY;
        worldMaxX = maxX;
        worldMaxY = maxY;
    }

    public void crash() {
        vx *= CRASH_SLOWDOWN;
        vy *= CRASH_SLOWDOWN;
        x -= vx * 3;
        y -= vy * 3;
    }

    public void draw(double camX, double camY, int viewX, int viewY, int viewWidth, int viewHeight) {
        double[] xpts = getCarXPoints();
        double[] ypts = getCarYPoints();

        for (int i = 0; i < xpts.length; i++) {
            xpts[i] = viewX + xpts[i] - camX + viewWidth / 2.0;
            ypts[i] = viewY + ypts[i] - camY + viewHeight / 2.0;
        }

        StdDraw.setPenColor(new Color(200, 25, 30));
        StdDraw.filledPolygon(xpts, ypts);
        StdDraw.setPenColor(new Color(30, 30, 30));
        StdDraw.setPenRadius(0.004);
        StdDraw.polygon(xpts, ypts);

        drawCarDetails(camX, camY, viewX, viewY, viewWidth, viewHeight);
    }

    private void drawCarDetails(double camX, double camY, int viewX, int viewY, int viewWidth, int viewHeight) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);

        double[] stripeX = {-2, 2, 4, 4, 2, -2, -4, -4};
        double[] stripeY = {15, 15, 8, -2, -8, -8, -2, 8};
        drawLocalPolygon(stripeX, stripeY, new Color(255, 220, 20), null,
                cos, sin, camX, camY, viewX, viewY, viewWidth, viewHeight);

        double[] cockpitX = {-3, 3, 4, 3, -3, -4};
        double[] cockpitY = {4, 4, -2, -5, -5, -2};
        drawLocalPolygon(cockpitX, cockpitY, new Color(50, 50, 80), null,
                cos, sin, camX, camY, viewX, viewY, viewWidth, viewHeight);

        double[] frontWingX = {-13, 13, 11, 11, -11, -11};
        double[] frontWingY = {8, 8, 6, 5, 5, 6};
        drawLocalPolygon(frontWingX, frontWingY, new Color(45, 45, 45), new Color(20, 20, 20),
                cos, sin, camX, camY, viewX, viewY, viewWidth, viewHeight);

        drawWheel(-11, -9, camX, camY, viewX, viewY, viewWidth, viewHeight, cos, sin);
        drawWheel(11, -9, camX, camY, viewX, viewY, viewWidth, viewHeight, cos, sin);
        drawWheel(-9, 6, camX, camY, viewX, viewY, viewWidth, viewHeight, cos, sin);
        drawWheel(9, 6, camX, camY, viewX, viewY, viewWidth, viewHeight, cos, sin);
    }

    private void drawLocalPolygon(double[] localX, double[] localY, Color fill, Color border,
                                  double cos, double sin, double camX, double camY,
                                  int viewX, int viewY, int viewWidth, int viewHeight) {
        double[] xpts = new double[localX.length];
        double[] ypts = new double[localY.length];
        for (int i = 0; i < localX.length; i++) {
            double worldX = x + localX[i] * -sin + localY[i] * cos;
            double worldY = y + localX[i] * cos + localY[i] * sin;
            xpts[i] = viewX + worldX - camX + viewWidth / 2.0;
            ypts[i] = viewY + worldY - camY + viewHeight / 2.0;
        }
        if (fill != null) {
            StdDraw.setPenColor(fill);
            StdDraw.filledPolygon(xpts, ypts);
        }
        if (border != null) {
            StdDraw.setPenColor(border);
            StdDraw.setPenRadius(0.003);
            StdDraw.polygon(xpts, ypts);
        }
    }

    private void drawWheel(double localX, double localY, double camX, double camY,
                           int viewX, int viewY, int viewWidth, int viewHeight,
                           double cos, double sin) {
        double worldX = x + localX * -sin + localY * cos;
        double worldY = y + localX * cos + localY * sin;
        double screenX = viewX + worldX - camX + viewWidth / 2.0;
        double screenY = viewY + worldY - camY + viewHeight / 2.0;
        StdDraw.setPenColor(new Color(25, 25, 25));
        StdDraw.filledEllipse(screenX, screenY, 3.0, 1.8);
        StdDraw.setPenColor(new Color(90, 90, 90));
        StdDraw.filledEllipse(screenX, screenY, 1.6, 0.8);
    }

    private double[] getCarXPoints() {
        double[] xs = new double[4];
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);

        double[] localX = {-15, 15, 15, -15};
        double[] localY = {10, 10, -10, -10};

        for (int i = 0; i < localX.length; i++) {
            xs[i] = x + localX[i] * -sin + localY[i] * cos;
        }

        return xs;
    }

    private double[] getCarYPoints() {
        double[] ys = new double[4];
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);

        double[] localX = {-15, 15, 15, -15};
        double[] localY = {10, 10, -10, -10};

        for (int i = 0; i < localX.length; i++) {
            ys[i] = y + localX[i] * cos + localY[i] * sin;
        }

        return ys;
    }

    public double getSpeed() {
        return Math.sqrt(vx * vx + vy * vy);
    }

    public double getAngle() {
        return angle;
    }
}
