import java.util.ArrayList;
import java.util.List;

/**
 * Created by giannis on 15/01/2015.
 */
public class Neighbors {

    public void setNodeName(String node_name) {
        this.node_name = node_name;
    }

    private String node_name;

    public List<Integer> getNeighbors() {
        return nei_lis;
    }

    //contains the neighbors of a node
    private List<Integer> nei_lis=new ArrayList<Integer>();

    public void addNeighbor(Integer nei){
        nei_lis.add(nei);
    }

    public String toString(){
        StringBuilder sb=new StringBuilder();

        for (Integer neighbor:nei_lis){
            sb.append(neighbor).append(",");
        }
        sb.setLength(sb.length()-1);
        return "Name:"+node_name+" points to:"+sb.toString();
    }
}
