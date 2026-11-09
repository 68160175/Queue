package Queue;

import java.util.Queue;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Iterator;

public class QueueManager {

    private Queue<Customer> waitingQueue;
    private ArrayList<Customer> historyList;

    private Customer currentCustomer;

    private int depositCount = 0;
    private int withdrawCount = 0;
    private int accountCount = 0;
    private int inquiryCount = 0;

    public QueueManager() {
        waitingQueue = new LinkedList<>();
        historyList = new ArrayList<>();
    }

    // สร้างหมายเลขคิว
    public Customer register(String name, String serviceType) {

        String prefix = "";

        switch (serviceType) {
            case "Deposit":
                prefix = "A";
                depositCount++;
                break;

            case "Withdraw":
                prefix = "B";
                withdrawCount++;
                break;

            case "Open Saving Account":
                prefix = "C";
                accountCount++;
                break;

            case "Request information":
                prefix = "D";
                inquiryCount++;
                break;
        }

        String queueNumber = prefix + String.format("%03d",
                getCount(serviceType));

        Customer customer = new Customer(
                queueNumber, name, serviceType);

        waitingQueue.offer(customer);
        historyList.add(customer);

        return customer;
    }

    // ตรวจสอบจำนวนคิวตามประเภท
    private int getCount(String serviceType) {

        switch (serviceType) {
            case "Deposit":
                return depositCount;
            case "Withdraw":
                return withdrawCount;
            case "Open Saving Account":
                return accountCount;
            case "Request information":
                return inquiryCount;
            default:
                return 0;
        }
    }

    // เรียกคิวถัดไป
    public Customer callNext() {

        if (currentCustomer != null) {
            return null;
        }

        currentCustomer = waitingQueue.poll();

        if (currentCustomer != null) {
            currentCustomer.setStatus("Serving");
        }

        return currentCustomer;
    }

    // เสร็จสิ้นการให้บริการ
    public void completeService() {

        if (currentCustomer != null) {
            currentCustomer.setStatus("Completed");
            currentCustomer = null;
        }
    }

    // ยกเลิกคิว
    public boolean cancelQueue(String queueNumber) {

        Iterator<Customer> iterator = waitingQueue.iterator();

        while (iterator.hasNext()) {

            Customer customer = iterator.next();

            if (customer.getQueueNumber().equals(queueNumber)) {

                iterator.remove();
                customer.setStatus("Cancelled");

                return true;
            }
        }

        return false;
    }

    // ดึงข้อมูลคิวที่กำลังรอ
    public Queue<Customer> getWaitingQueue() {
        return waitingQueue;
    }

    // ดึงประวัติทั้งหมด
    public ArrayList<Customer> getHistoryList() {
        return historyList;
    }

    // ดึงลูกค้าที่กำลังรับบริการ
    public Customer getCurrentCustomer() {
        return currentCustomer;
    }

    // จำนวนคิวที่รอ
    public int getWaitingCount() {
        return waitingQueue.size();
    }

    // จำนวนลูกค้าทั้งหมด
    public int getTotalCount() {
        return historyList.size();
    }

    // จำนวนที่บริการเสร็จแล้ว
    public int getCompletedCount() {

        int count = 0;

        for (Customer customer : historyList) {

            if (customer.getStatus().equals("Completed")) {
                count++;
            }
        }

        return count;
    }
}