public class BFSTest {
    public void BFS() {

    }
}

/*
Logic: BFS works like so:

Begin with a path with branches.
Take the starting position on that path.
Put the starting position in the queue.

Loop:
Remove the first element in the queue.
Process it by finding its neighbors.
Put it in visited.
Put its neighbors in the queue.

IF:
A neighbor has been visited: gets its neighbors and put those in queue.
If no neighbors: put the node in queue and go to the next processable item.
If both are true: go to next item in queue.

Parent:
The first node has no parent.
Track all paths by having the parent (LinkedList) path being of the nodes from where the current node came from.
*/