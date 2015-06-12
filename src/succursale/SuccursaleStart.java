package succursale;

import java.io.IOException;

/**
 * Created by Gus on 6/11/2015.
 */
public class SuccursaleStart {
    public static void main(String[] args){

        try {
            new Client();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
