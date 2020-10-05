package root;
import java.util.Observable;
import static root.controllerRootView.fin;
import static root.controllerRootView.score;


public class Hilo extends Observable implements Runnable  {
    private int time;
    private String data;
    public static boolean actualizar = false;
    public Hilo(){
        time = 10;
    }
    @Override
    public void run() {
        while(!fin){
            System.out.println(time);
            data = time +"";
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            time--;
            actualizar = false;
            if(score>0) score-=10;
            if(time==-1){
                time=10;
                actualizar = true;
            }
            data += " "+ actualizar;
            setChanged();
            notifyObservers(data);
        }

    }
}
