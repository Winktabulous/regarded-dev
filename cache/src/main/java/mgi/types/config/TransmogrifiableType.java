package mgi.types.config;

/**
 * @author Kris | 22/11/2018 20:23
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public interface TransmogrifiableType {

    int getVarbitId();
    int getVarpId();
    int[] getTransmogrifiedIds();
    int defaultId();

}
