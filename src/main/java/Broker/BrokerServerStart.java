package Broker;

import Dao.AnimalPartDao;
import Dao.ProductDao;
import Dao.TrayDao;

import java.sql.SQLException;

public class BrokerServerStart {
    public static void main(String[] args) throws Exception {
        BrokerServer server = new BrokerServer(AnimalPartDao.getInstance(), ProductDao.getInstance(), TrayDao.getInstance());
        server.run();
    }
}
