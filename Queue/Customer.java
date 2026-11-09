package Queue;

public class Customer {

    private String queueNumber;
    private String name;
    private String serviceType;
    private String status;

    public Customer(String queueNumber, String name, String serviceType) {
        this.queueNumber = queueNumber;
        this.name = name;
        this.serviceType = serviceType;
        this.status = "Waiting";
    }

    public String getQueueNumber() {
        return queueNumber;
    }

    public String getName() {
        return name;
    }

    public String getServiceType() {
        return serviceType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}