package AppServer;

import Dao.AnimalDao;
import Dao.AnimalPartDao;
import Dao.TrayDao;
import io.grpc.Server;
import io.grpc.ServerBuilder;

public class ServerStart {
    public static void main(String[] args) {
        try {
            Server server = ServerBuilder.forPort(8081).addService(new AnimalServiceImpl(AnimalDao.getInstance())).addService(new AnimalPartServiceImpl(
                AnimalPartDao.getInstance())).addService(new TrayServiceImpl(
                TrayDao.getInstance(), AnimalPartDao.getInstance())).build();
            server.start();
            System.out.println("Database access server started at port 8081.");
            server.awaitTermination();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
