import java.util.ArrayList;

public class SaveRandom {
    ArrayList<Integer> randomNum = new ArrayList<>();

    public SaveRandom(){
        randomNum.add(Rand.r(1,9));
    }

    public void BenOverLapRandomNum(){
        while(randomNum.size() != 3){
            int temp = Rand.r(1,9);
            while(randomNum.contains(temp)){
                temp = Rand.r(1,9);
            }
            randomNum.add(temp);
        }
    }
}


