/*
F1Game.java

A simple 2D top-down Formula 1 racing game using StdDraw for graphics.
Features multiple tracks, car physics, lap timing, and a main menu.

Controls:
- Accelerate: W or Up Arrow
- Brake: S or Down Arrow
- Steer Left: A or Left Arrow
- Steer Right: D or Right Arrow

In the main menu, click on a track to select it, then click "Start Race"

Prompts:
make a working f1 game with realistic phsyics in traction and acceleration and velocity. Use the stdDraw library and display the car and track.

fix the steering and enlarge the track. collision detect with the barriers.

make the track 1:1 scale with the car, and add a racing line for ease of use.

add a timer and finish/start line to record best lap and current lap, and abandon the lap if there is a collision.

fix the track, the track is not generating properly. II also do not see a start/ finish line. make the start/finish line a checker b/w pattern to be visible.
the car is also getting stuck.

make the start/finish line a line, not a dot. remove crash phsyics and only slow the car down.

on the long track, there are two tracks. fix this bug. also, start the timer when the car starts across the line and end it when it goes across the line on the other side. if the itme is a new best, add it to the best time.

on monza, there is a bug where theres two tracks. fix it.

add a red blinking animation to when a new best time is set before returning the time to white.

the timer still does not work.

the red blinking animation is not playing.

replace the long circuit, it is too confusing. also, the animation is still not working.

make the area that is not track realstic grass, add texture to the black track, and fix the track so that it is more defined where to turn. There is also a bug in hairpin corners where the track looks weird and the racing line is off. Add a realistic shape to the car as well.

redraw monza, it has bugs

enable arrow key controls

fix the direction of the car sprite

Create a game menu. When the program is run, it opens a menu with all the track to play, the user can pick any of the tracks. all of the track shapes are also displayed along with the track names. There should be a button in the top right to exit a track, and a user’s scores for that track are stored in that track. 

fix the menu orientation.

the exit sign is in the tracks, not the menu. include an exit sing in each track that exits to the menu.

polish all of the visuals and include smooth transition animations between screens.

i want you to polish the menu by highlighting a button when it is hovered over, and providing a smoother transition (like a slide) to improve quality.

there is an error where the track best time is stored globally, not separate for each track. make sure each track has its own indivudual best time.

implement a mechanic that when a lap is completed, it shows how faster/slower the lap was from the best lap, and display this number next to the best lap with a +/-.

create a ghost car of the best lap and make it 30% transparent.

resize the window to be larger

resize the menu to fit the new window and add 3 more tracks.

move all the white boxes up, and also make all the tracks more intersting while keeping them playable.

the window for viewing the car is a bit large, making it laggy. resize the window to make it less laggy.

no, make the window bigger but the grass box smaller

the grass window is misaligned and bugged, fix it

Author: Rohan Balasubramanian (coded with help from Copilot)
Language: Java 24.0.2
DOC: 6/9/2026
*/

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.util.Map;

public class F1Game {
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 800;
    private static final int VIEW_WIDTH = 900;
    private static final int VIEW_HEIGHT = 600;
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
    private Map<String, java.util.List<double[]>> bestLapPaths;
    private java.util.List<double[]> bestLapPath;
    private java.util.List<double[]> currentLapPath;
    private long bestLapTime = Long.MAX_VALUE;
    private Long lastLapDelta = null; // ms difference: lap - best
    private long lastLapDeltaTime = 0;
    private boolean inMenu = true;
    private boolean mouseWasPressed = false;
    private int hoveredTrackIndex = -1;
    private int selectedTrackIndex = 0;
    // Transition animation state
    private boolean transitioning = false;
    private int transitionDirection = 0; // 1: menu->game, -1: game->menu
    private double transitionProgress = 0.0; // 0..1
    private static final double TRANSITION_DURATION = 0.5; // seconds
    private int pendingSelectedIndex = -1;

    // audio removed

    public F1Game() {
        car = new Car(WIDTH / 2, HEIGHT / 2);
        bestLapTimes = new java.util.HashMap<>();
        bestLapPaths = new java.util.HashMap<>();
        currentLapPath = new java.util.ArrayList<>();
        bestLapPath = null;
        initializeTracks();
    }

    private void initializeTracks() {
        this.tracks = new TrackDefinition[]{
            new TrackDefinition("Monaco", new double[][]{
                {520, 350}, {470, 420}, {390, 480}, {310, 500}, {240, 450},
                {210, 350}, {240, 250}, {330, 200}, {420, 190}, {520, 210},
                {570, 280}, {590, 350}, {550, 400}, {520, 350}
            }),
            new TrackDefinition("Silverstone", new double[][]{
                {200, 100}, {480, 70}, {820, 110}, {920, 280}, {860, 420},
                {700, 480}, {520, 520}, {350, 500}, {200, 380}, {180, 250},
                {190, 180}, {280, 130}, {380, 100}
            }),
            new TrackDefinition("Long Circuit", new double[][]{
                {180, 160}, {450, 140}, {750, 150}, {950, 300}, {850, 520},
                {520, 540}, {280, 480}, {180, 320}, {200, 200}
            }),
            new TrackDefinition("Monza", new double[][]{
                {220, 240}, {380, 180}, {600, 200}, {800, 300}, {820, 380},
                {680, 480}, {480, 520}, {320, 450}, {240, 350}, {220, 240}
            }),
            new TrackDefinition("Suzuka Figure-8", new double[][]{
                {150, 300}, {250, 150}, {380, 130}, {520, 160}, {600, 260},
                {580, 350}, {480, 420}, {380, 440}, {300, 400}, {250, 310},
                {220, 240}, {280, 160}, {380, 140}
            }),
            new TrackDefinition("Spa", new double[][]{
                {280, 150}, {460, 100}, {700, 140}, {850, 310}, {780, 480},
                {600, 550}, {380, 540}, {220, 450}, {180, 320}, {220, 200}
            }),
            new TrackDefinition("Istanbul", new double[][]{
                {320, 220}, {520, 140}, {760, 170}, {900, 340}, {760, 480}, {500, 540}, {300, 460}, {240, 310}
            }),
            new TrackDefinition("Nurburgring", new double[][]{
                {220, 330}, {360, 220}, {560, 180}, {780, 220}, {920, 360}, {840, 480}, {600, 540}, {400, 500}, {260, 400}
            }),
            new TrackDefinition("Interlagos Tight", new double[][]{
                {260, 250}, {380, 160}, {540, 150}, {700, 200}, {800, 320}, {720, 440}, {520, 500}, {340, 440}, {260, 320}
            })
        };

        int selectedIndex = (int) (Math.random() * this.tracks.length);
        selectedTrackIndex = selectedIndex;
        TrackDefinition selected = this.tracks[selectedIndex];
        trackName = selected.name;
        bestLapPath = bestLapPaths.get(trackName);
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
                    currentLapPath.clear();
                    currentLapPath.add(new double[]{x2, y2, car.getAngle()});
                } else {
                    long lapTime = now - lapStartTime;
                    if (lapTime > 500) {
                            // compute delta compared to current best before updating
                            if (bestLapTime == Long.MAX_VALUE) {
                                lastLapDelta = null;
                            } else {
                                lastLapDelta = lapTime - bestLapTime;
                                lastLapDeltaTime = now;
                            }
                            if (lapTime < bestLapTime) {
                                currentLapPath.add(new double[]{car.getX(), car.getY(), car.getAngle()});
                                bestLapTime = lapTime;
                                bestLapTimes.put(trackName, lapTime);
                                bestLapPaths.put(trackName, new java.util.ArrayList<>(currentLapPath));
                                bestLapPath = bestLapPaths.get(trackName);
                                bestLapBlinkStart = now;
                            }
                        lapStartTime = now;
                        currentLapPath.clear();
                        currentLapPath.add(new double[]{x2, y2, car.getAngle()});
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
                        // load pending track and enter game at halfway
                        if (pendingSelectedIndex >= 0) {
                            selectTrack(pendingSelectedIndex);
                            pendingSelectedIndex = -1;
                        }
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
                    if (lapTimingStarted) {
                        currentLapPath.add(new double[]{car.getX(), car.getY(), car.getAngle()});
                    }
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
        }
        if (brakingNow) {
            car.brake(BRAKE_DECELERATION);
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
                double availableWidth = WIDTH - 2 * MENU_MARGIN;
                double previewWidth = (availableWidth - (MENU_COLUMNS - 1) * MENU_MARGIN) / MENU_COLUMNS;
                double previewHeight = MENU_PREVIEW_HEIGHT;

                // Check track selection
                for (int i = 0; i < tracks.length; i++) {
                    if (isMouseInTrackButton(i, mouseX, mouseY, previewWidth, previewHeight)) {
                        selectTrack(i);
                        mouseWasPressed = true;
                        return;
                    }
                }

                // Check start button click
                double startW = 160;
                double startH = 40;
                double startX = WIDTH - 120;
                double startY = 80;
                if (mouseX >= startX - startW / 2.0 && mouseX <= startX + startW / 2.0 &&
                    mouseY >= startY - startH / 2.0 && mouseY <= startY + startH / 2.0) {
                    inMenu = false;
                    lapTimingStarted = false;
                    lapStartTime = 0;
                    mouseWasPressed = true;
                    return;
                }
            }
            mouseWasPressed = true;
        } else {
            mouseWasPressed = false;
        }
    }

    // audio removed

    private boolean isMouseInTrackButton(int trackIndex, double mouseX, double mouseY,
                                           double previewWidth, double previewHeight) {
        int col = trackIndex % MENU_COLUMNS;
        int row = trackIndex / MENU_COLUMNS;
        double x = MENU_MARGIN + col * (previewWidth + MENU_MARGIN);
        double y = MENU_TOP_OFFSET + row * (previewHeight + MENU_MARGIN);
        return mouseX >= x && mouseX <= x + previewWidth &&
               mouseY >= y && mouseY <= y + previewHeight;
    }

    private void selectTrack(int index) {
        selectedTrackIndex = index;
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
        
        // Load best lap time and path for this track
        bestLapTime = bestLapTimes.getOrDefault(trackName, Long.MAX_VALUE);
        bestLapPath = bestLapPaths.get(trackName);
        currentLapPath.clear();
        lastLapDelta = null;
        lastLapDeltaTime = 0;
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
            drawGhostCar(camX, camY);

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
            StdDraw.rectangle(VIEW_X + VIEW_WIDTH / 2.0, VIEW_Y + VIEW_HEIGHT / 2.0 + 20,
                    VIEW_WIDTH / 2.0, VIEW_HEIGHT / 2.0);

            car.draw(camX, camY, VIEW_X, VIEW_Y, VIEW_WIDTH, VIEW_HEIGHT);
            drawHUD();
            drawExitButton();
        }
        StdDraw.show();
    }

    private void drawMenu() {
        // Soft vertical gradient background
        for (int i = 0; i < HEIGHT; i += 2) {
            float f = (float) i / HEIGHT;
            int r = (int) (30 + 80 * f);
            int g = (int) (40 + 90 * f);
            int b = (int) (60 + 140 * f);
            StdDraw.setPenColor(new Color(r, g, b));
            StdDraw.filledRectangle(WIDTH / 2.0, i + 1, WIDTH / 2.0, 1);
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

        // Compute responsive grid for track buttons to fill the window
        int columns = MENU_COLUMNS;
        double availableWidth = WIDTH - 2 * MENU_MARGIN;
        double previewWidth = (availableWidth - (columns - 1) * MENU_MARGIN) / columns;
        double previewHeight = MENU_PREVIEW_HEIGHT;
        int rows = (tracks.length + columns - 1) / columns;
        // center the grid horizontally and position it below the top card
        double gridWidth = columns * previewWidth + (columns - 1) * MENU_MARGIN;
        double gridStartX = (WIDTH - gridWidth) / 2.0 + previewWidth/2.0;
        double gridStartY = MENU_TOP_OFFSET;

        hoveredTrackIndex = -1;
        for (int i = 0; i < tracks.length; i++) {
            int col = i % columns;
            int row = i / columns;
            double centerX = gridStartX + col * (previewWidth + MENU_MARGIN);
            double centerY = gridStartY + row * (previewHeight + MENU_MARGIN) + previewHeight/2.0;
            double x = centerX - previewWidth/2.0;
            double y = centerY - previewHeight/2.0;

            // Update hover state
            double mx = StdDraw.mouseX();
            double my = StdDraw.mouseY();
            if (isMouseInTrackButton(i, mx, my, previewWidth, previewHeight)) {
                hoveredTrackIndex = i;
            }

            // Shadow
            StdDraw.setPenColor(new Color(0, 0, 0, 100));
            StdDraw.filledRectangle(centerX + 4, centerY - 4, previewWidth / 2.0, previewHeight / 2.0);

            // Button background
            if (i == selectedTrackIndex) {
                StdDraw.setPenColor(new Color(210, 230, 255));
            } else if (i == hoveredTrackIndex) {
                StdDraw.setPenColor(new Color(250, 250, 230));
            } else {
                StdDraw.setPenColor(new Color(245, 245, 245));
            }
            StdDraw.filledRectangle(centerX, centerY, previewWidth / 2.0, previewHeight / 2.0);

            // Track layout preview (fit into the box with padding)
            double padding = 12;
            drawTrackLayoutPreview(tracks[i], x + padding, y + padding, previewWidth - padding*2, previewHeight - padding*2);

            // Border
            StdDraw.setPenRadius(0.003);
            StdDraw.setPenColor(new Color(30, 30, 30));
            StdDraw.rectangle(centerX, centerY, previewWidth / 2.0, previewHeight / 2.0);

            // Track name
            StdDraw.setFont(new Font("Arial", Font.BOLD, 14));
            StdDraw.setPenColor(new Color(10, 10, 10));
            StdDraw.text(x + previewWidth * 0.7, y + 22, tracks[i].name);
            // show best lap for this track if available
            long b = bestLapTimes.getOrDefault(tracks[i].name, Long.MAX_VALUE);
            String best = (b == Long.MAX_VALUE) ? "--:--.---" : formatTime(b);
            StdDraw.setFont(new Font("Arial", Font.PLAIN, 12));
            StdDraw.setPenColor(new Color(80, 80, 80));
            StdDraw.text(x + previewWidth * 0.7, y + 6, "Best: " + best);
        }

        // Start button
        double startW = 160, startH = 40;
        double startX = WIDTH - 120, startY = 80;
        StdDraw.setPenColor(new Color(220, 60, 60));
        StdDraw.filledRectangle(startX, startY, startW / 2.0, startH / 2.0);
        StdDraw.setPenColor(new Color(255, 255, 255, 200));
        StdDraw.setPenRadius(0.004);
        StdDraw.rectangle(startX, startY, startW / 2.0, startH / 2.0);
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
        double pad = 6;
        double scale = Math.min((width - pad*2) / trackWidth, (height - pad*2) / trackHeight);
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

    private double screenToWorldX(double screenX, double camX) {
        return camX + screenX - VIEW_X - VIEW_WIDTH / 2.0;
    }

    private double screenToWorldY(double screenY, double camY) {
        return camY + screenY - VIEW_Y - VIEW_HEIGHT / 2.0;
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

    private void drawGhostCar(double camX, double camY) {
        if (bestLapPath == null || bestLapPath.isEmpty()) {
            return;
        }
        double ratio = 0.0;
        if (lapTimingStarted && bestLapTime != Long.MAX_VALUE) {
            long now = System.currentTimeMillis();
            ratio = Math.min(1.0, (double) (now - lapStartTime) / Math.max(1, bestLapTime));
        }
        int index = (int) Math.round(ratio * (bestLapPath.size() - 1));
        index = Math.max(0, Math.min(bestLapPath.size() - 1, index));
        double[] ghost = bestLapPath.get(index);
        car.drawGhost(ghost[0], ghost[1], ghost[2], camX, camY,
                VIEW_X, VIEW_Y, VIEW_WIDTH, VIEW_HEIGHT);
    }

    private void drawGrassTexture(double camX, double camY) {
        // Fill the view area with a dark green base for the grass
        StdDraw.setPenColor(new Color(22, 120, 30));
        StdDraw.filledRectangle(VIEW_X + VIEW_WIDTH / 2.0, VIEW_Y + VIEW_HEIGHT / 2.0,
            VIEW_WIDTH / 2.0, VIEW_HEIGHT / 2.0);

        // Draw a simple grass texture over the fill
        StdDraw.setPenRadius(0.002);
        for (double sx = VIEW_X; sx <= VIEW_X + VIEW_WIDTH; sx += 20) {
            for (double sy = VIEW_Y; sy <= VIEW_Y + VIEW_HEIGHT; sy += 20) {
                double wx = screenToWorldX(sx, camX);
                double wy = screenToWorldY(sy, camY);
                int ix = (int) Math.round(wx);
                int iy = (int) Math.round(wy);
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
        StdDraw.text(WIDTH / 2.0, HEIGHT - 50, "Track: " + trackName);
        StdDraw.text(90, HEIGHT - 50, String.format("Speed: %.1f", car.getSpeed()));
        // Lap timing display
        String currentLap = "--:--.---";
        if (lapTimingStarted) {
            currentLap = formatTime(now - lapStartTime);
        }
        String bestLap = (bestLapTime == Long.MAX_VALUE) ? "--:--.---" : formatTime(bestLapTime);
        StdDraw.text(90, HEIGHT - 90, "Lap: " + currentLap);
        if (drawBestLap) {
            StdDraw.setPenColor(new Color(255, 215, 0));
            StdDraw.text(90, HEIGHT - 110, "Best: " + bestLap);
            StdDraw.setPenColor(StdDraw.WHITE);
        }

        int controlsY = HEIGHT - 130;
        if (lastLapDelta != null && now - lastLapDeltaTime < 5000) {
            long delta = lastLapDelta;
            long absDelta = Math.abs(delta);
            String deltaSign = delta < 0 ? "-" : "+";
            String deltaText = String.format("%s%02d:%02d.%03d", deltaSign,
                    absDelta / 60000, (absDelta / 1000) % 60, absDelta % 1000);
            StdDraw.setPenColor(delta < 0 ? new Color(100, 255, 100) : new Color(255, 120, 120));
            StdDraw.text(90, HEIGHT - 130, "Δ: " + deltaText);
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.text(90, HEIGHT - 150, delta < 0 ? "Time gained" : "Time lost");
            controlsY = HEIGHT - 170;
        }
        StdDraw.text(90, controlsY, "W/up:Accelerate S/down:Brake A/left:Left D/right:Right");
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

    public void drawGhost(double gx, double gy, double gangle,
                          double camX, double camY, int viewX, int viewY,
                          int viewWidth, int viewHeight) {
        double cos = Math.cos(gangle);
        double sin = Math.sin(gangle);

        double[] xpts = new double[4];
        double[] ypts = new double[4];
        double[] localX = {-15, 15, 15, -15};
        double[] localY = {10, 10, -10, -10};

        for (int i = 0; i < xpts.length; i++) {
            double worldX = gx + localX[i] * -sin + localY[i] * cos;
            double worldY = gy + localX[i] * cos + localY[i] * sin;
            xpts[i] = viewX + worldX - camX + viewWidth / 2.0;
            ypts[i] = viewY + worldY - camY + viewHeight / 2.0;
        }

        Color fill = new Color(200, 25, 30, 77);
        Color border = new Color(30, 30, 30, 77);
        StdDraw.setPenColor(fill);
        StdDraw.filledPolygon(xpts, ypts);
        StdDraw.setPenColor(border);
        StdDraw.setPenRadius(0.004);
        StdDraw.polygon(xpts, ypts);

        drawGhostDetails(gx, gy, gangle, camX, camY, viewX, viewY, viewWidth, viewHeight);
    }

    private void drawGhostDetails(double gx, double gy, double gangle,
                                  double camX, double camY, int viewX, int viewY,
                                  int viewWidth, int viewHeight) {
        double cos = Math.cos(gangle);
        double sin = Math.sin(gangle);

        double[] stripeX = {-2, 2, 4, 4, 2, -2, -4, -4};
        double[] stripeY = {15, 15, 8, -2, -8, -8, -2, 8};
        drawGhostLocalPolygon(stripeX, stripeY, new Color(255, 220, 20, 77), null,
                cos, sin, gx, gy, camX, camY, viewX, viewY, viewWidth, viewHeight);

        double[] cockpitX = {-3, 3, 4, 3, -3, -4};
        double[] cockpitY = {4, 4, -2, -5, -5, -2};
        drawGhostLocalPolygon(cockpitX, cockpitY, new Color(50, 50, 80, 77), null,
                cos, sin, gx, gy, camX, camY, viewX, viewY, viewWidth, viewHeight);

        double[] frontWingX = {-13, 13, 11, 11, -11, -11};
        double[] frontWingY = {8, 8, 6, 5, 5, 6};
        drawGhostLocalPolygon(frontWingX, frontWingY, new Color(45, 45, 45, 77),
                new Color(20, 20, 20, 77), cos, sin, gx, gy, camX, camY,
                viewX, viewY, viewWidth, viewHeight);

        drawGhostWheel(-11, -9, gx, gy, camX, camY, viewX, viewY, viewWidth, viewHeight, cos, sin);
        drawGhostWheel(11, -9, gx, gy, camX, camY, viewX, viewY, viewWidth, viewHeight, cos, sin);
        drawGhostWheel(-9, 6, gx, gy, camX, camY, viewX, viewY, viewWidth, viewHeight, cos, sin);
        drawGhostWheel(9, 6, gx, gy, camX, camY, viewX, viewY, viewWidth, viewHeight, cos, sin);
    }

    private void drawGhostLocalPolygon(double[] localX, double[] localY, Color fill, Color border,
                                       double cos, double sin, double gx, double gy,
                                       double camX, double camY, int viewX, int viewY,
                                       int viewWidth, int viewHeight) {
        double[] xpts = new double[localX.length];
        double[] ypts = new double[localY.length];
        for (int i = 0; i < localX.length; i++) {
            double worldX = gx + localX[i] * -sin + localY[i] * cos;
            double worldY = gy + localX[i] * cos + localY[i] * sin;
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

    private void drawGhostWheel(double localX, double localY, double gx, double gy,
                           double camX, double camY, int viewX, int viewY,
                           int viewWidth, int viewHeight, double cos, double sin) {
        double worldX = gx + localX * -sin + localY * cos;
        double worldY = gy + localX * cos + localY * sin;
        double screenX = viewX + worldX - camX + viewWidth / 2.0;
        double screenY = viewY + worldY - camY + viewHeight / 2.0;
        StdDraw.setPenColor(new Color(25, 25, 25, 77));
        StdDraw.filledEllipse(screenX, screenY, 3.0, 1.8);
        StdDraw.setPenColor(new Color(90, 90, 90, 77));
        StdDraw.filledEllipse(screenX, screenY, 1.6, 0.8);
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
