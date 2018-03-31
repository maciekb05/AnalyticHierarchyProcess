import java.util.LinkedList;
import java.util.Queue;

class RankAHP {

    static void evaluateRanks(Representation representation, Integer methodNumber) {
        Queue<ElementAHP> queue = new LinkedList<>();
        queue.add(representation.root);
        while(!queue.isEmpty()){
            ElementAHP current = queue.poll();
            current.rank = AlgorithmsAHP.rankMethod(current.matrix,methodNumber);
            if(current.children.size() != 0){
                queue.addAll(current.children);
            }
        }
    }

    static LinkedList<Double> ranking(Representation representation) {

        ElementAHP current = representation.root;
        Queue<ElementAHP> queue = new LinkedList<>();

        LinkedList<Double> ranking = new LinkedList<>();
        for(int i = 0; i < representation.choices.size(); i++) {
            ranking.add(0.0);
        }

        while(true){
            if(current.children.size()!=0) {
                queue.addAll(current.children);
                if(current != representation.root) {
                    calculateRank(current);
                }
                current = queue.poll();
            }
            else{
                calculateRank(current);
                for(int i = 0; i < representation.choices.size(); i++) {
                    ranking.set(i, ranking.get(i) + current.rank.get(i));
                }
                if(queue.isEmpty()){
                    break;
                }
                current = queue.poll();
            }
        }
        return ranking;
    }

    private static void calculateRank(ElementAHP elementAHP){
        Integer index = elementAHP.parent.children.indexOf(elementAHP);
        for (int i = 0; i < elementAHP.rank.size(); i++) {
            elementAHP.rank.set(i, elementAHP.parent.rank.get(index) * elementAHP.rank.get(i));
        }
    }

}
