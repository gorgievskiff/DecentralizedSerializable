package DecentralizedVSerializable;

import java.io.Serializable;
//- тип: storage/compute
//- ram: во мегабајти
//- број јадра
//- број секунди за извршување

public class RequestMessage implements Serializable {
    private String StorageCompute;
    private int Ram;
    private int Cores;
    private int Seconds;


    public RequestMessage() {
    }

    public String getStorageCompute() {
        return StorageCompute;
    }

    public void setStorageCompute(String storageCompute) {
        StorageCompute = storageCompute;
    }

    public int getRam() {
        return Ram;
    }

    public void setRam(int ram) {
        Ram = ram;
    }

    public int getCores() {
        return Cores;
    }

    public void setCores(int cores) {
        Cores = cores;
    }

    public int getSeconds() {
        return Seconds;
    }

    public void setSeconds(int seconds) {
        Seconds = seconds;
    }
}
