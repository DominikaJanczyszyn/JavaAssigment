package AppServer;

import Dao.AnimalDao;
import io.grpc.Server;
import io.grpc.ServerBuilder;

public class ServerStart {
    public static void main(String[] args) {
        try {
            Server server = ServerBuilder.forPort(8081).addService(new AnimalServiceImpl(AnimalDao.getInstance())).build();
            server.start();
            server.awaitTermination();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
