import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by Giannis on 14/01/2015.
 */
public class Organization {

    private final static int FILE_POS = 0;
    private final static int START_POS = 1;
    private final static int END_POS = 2;
    private final static int EMPLOYEE_POS = 1;
    private final static int NAME_POS = 2;
    private final static int MANID_POS = 3;
    //contains mappings between employee id's and their neighbors
    //essentially an undirected graph
    private Map<Integer, Neighbors> ugraph = new HashMap<Integer, Neighbors>();
    //contains mappings between employee names and their id's
    private Map<Integer, String> names = new HashMap<Integer, String>();
    //the path between start node and end node
    private Map<Integer, Integer> path = new HashMap<Integer, Integer>();
    private Integer start_id;
    private Integer end_id;
    private String file;
    private String start_name;
    private String end_name;


    public Organization(String file, String start, String end) {
        start_name = start;
        end_name = end;
        this.file = file;
    }

    /**
     * Print the undirected graph.
     * Used for validation purposes
     */
    public void printGraph() {
        Set<Map.Entry<Integer, Neighbors>> entries = ugraph.entrySet();

        for (Map.Entry<Integer, Neighbors> entry : entries) {
            System.out.println("Node id:" + entry.getKey() + " " + entry.getValue());
        }

    }

    /**
     * Read the file from
     * the file system
     */
    public void readFile() {
        FileReader fr = null;
        try {
            //try to open the file
            fr = new FileReader(file);
        } catch (FileNotFoundException e) {
            System.err.println("File does not exist");
            e.printStackTrace();
            System.exit(0);
        }
        BufferedReader bf = new BufferedReader(fr);
        String line = "";
        try {
            line = bf.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //read file line by line
        while (line != null) {
            //read one line
            try {
                line = bf.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (line == null) {
                break;
            }
            //remove trailing and leading blank and blank
            //characters between words chars and split line on |
            String[] tokens = line.trim().replaceAll("\\s+", " ").split("\\|");

            //get the employee id
            Integer eid = Integer.parseInt(tokens[EMPLOYEE_POS].trim());
            //get the employee name
            String employeename = tokens[NAME_POS].trim();
            if (employeename.equals(start_name)) {
                start_id = eid;
            }
            if (employeename.equals(end_name)) {
                end_id = eid;
            }
            //get the employee neighbor
            Integer neid;
            try {
                neid = Integer.parseInt(tokens[MANID_POS].trim());
            } catch (Exception e) {
                neid = -1;
            }

            names.put(eid, employeename);
            //add employee's id and name to map
            Neighbors employee_neis = ugraph.get(eid);
            //no corresponding entry
            if (employee_neis == null) {
                employee_neis = new Neighbors();
                ugraph.put(eid, employee_neis);
            }
            employee_neis.setNodeName(employeename);
            employee_neis.addNeighbor(neid);

            //add the edge of the opposite direction
            Neighbors neighbor_obj = ugraph.get(neid);
            if (neighbor_obj == null) {
                neighbor_obj = new Neighbors();
                ugraph.put(neid, neighbor_obj);
            }
            neighbor_obj.addNeighbor(eid);

        }
    }


    /**
     * Run the Dijkstra Algorithm
     */
    public void dijkstraAlgorithm() {
        if(start_id==null){
            System.err.println("The starting node does not exist");
            System.exit(1);
        }
        else if(end_id==null){
            System.err.println("The ending node does not exist");
            System.exit(1);
        }
        int current = start_id;
        //contains distances between every node and the start node
        HashMap<Integer, Integer> distances = new HashMap<Integer, Integer>();
        //set of unvisited nodes
        Set<Integer> unvisited = new HashSet<Integer>();
        Set<Integer> visited = new HashSet<Integer>();

        initDistances(distances, start_id);
        //mark all nodes except start_id node as unvisited
        markUnvisited(unvisited, start_id);


        while (true) {
            //get the neighbors of current node
            List<Integer> neighs = ugraph.get(current).getNeighbors();

            for (Integer neighbor : neighs) {
                //if visited or the root
                if (visited.contains(neighbor) || neighbor == -1) {
                    continue;
                }

                Integer neighbor_distance = distances.get(neighbor);
                //+1 since this graph is undirected
                int this_distance = distances.get(current) + 1;
                if (this_distance < neighbor_distance) {
                    distances.put(neighbor, this_distance);
                    path.put(neighbor, current);
                }
            }
            //mark current node as visited
            visited.add(current);
            //remove current from unvisited set
            unvisited.remove(current);

            //Select the unvisited node that is
            // marked with the smallest distance
            current = smallestUnivisited(unvisited, distances);
            //no unvisited node with smallest distance
            if (current == -1) {
                break;
            }
            //destination node reached
            if (visited.contains(end_id)) {
                break;
            }
        }
    }

    /**
     * Return the node id with
     * the smallest distance
     *
     * @param unvisited all unvisited nodes
     * @param distances distances from starting node
     * @return node id of the nearest node
     */
    private int smallestUnivisited(Set<Integer> unvisited, HashMap<Integer, Integer> distances) {
        int min = Integer.MAX_VALUE;
        int min_id = -1;
        for (Integer unvis : unvisited) {
            Integer dis = distances.get(unvis);
            if (dis < min) {
                min = dis;
                min_id = unvis;
            }

        }
        return min_id;
    }


    /**
     * Mark all nodes except the
     * starting node as unvisited
     *
     * @param unvisited all unvisited nodes
     * @param startid   start node id
     */
    private void markUnvisited(Set<Integer> unvisited, Integer startid) {
        Set<Integer> allnodes = ugraph.keySet();

        for (Integer node_id : allnodes) {
            unvisited.add(node_id);
        }
        //start node. distance : 0
        unvisited.remove(startid);
    }

    /**
     * Initialize all distances to MAX Integer
     * except for the starting node which
     * should be set to zero
     *
     * @param mp       distances  from starting node
     * @param start_id start node id
     */
    private void initDistances(HashMap<Integer, Integer> mp, Integer start_id) {
        Set<Integer> allnodes = ugraph.keySet();

        for (Integer node_id : allnodes) {
            mp.put(node_id, Integer.MAX_VALUE);
        }
        //start node. distance : 0
        mp.put(start_id, 0);
    }

    /**
     * Print the shortest path form
     * the start node to the end node
     */
    public void printPath() {
        int current = end_id;
        StringBuilder sb = new StringBuilder();
        while (true) {
            sb.insert(0, " (" + current + ") ");
            sb.insert(0, names.get(current));
            if (start_id == current)
                break;
            else
                sb.insert(0, "- ");
            current = path.get(current);
        }
        sb.lastIndexOf(sb.toString(), 1);

        System.out.println(sb.toString());
    }

    public static void main(String[] args) {


        if (args.length != 3) {
            System.err.println("Usage: MyProgram superheroes.txt \"Batman\" \"Super Ted\"");
            System.exit(0);
        }
        Organization or = new Organization(args[FILE_POS], args[START_POS], args[END_POS]);
        or.readFile();
        //or.printGraph();
        or.dijkstraAlgorithm();
        or.printPath();
    }
}
