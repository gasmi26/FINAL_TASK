class BankAccount {

  private double balance;

  public BankAccount(double initialBalance) {
    this.balance = initialBalance;
  }

  public synchronized void deposit(double amount) {
    balance += amount;
    System.out.println("Deposited: " + amount + ", New Balance: " + balance);
  }

  public synchronized void withdraw(int amount) throws InsufficientFundsException { 
    if (balance < amount) {
      throw new InsufficientFundsException("Insufficient funds for withdrawal.");
    }
    balance -= amount;
    System.out.println("Withdrew: " + amount + ", New Balance: " + balance);
  }
}

class Transaction {
  private final double amount;

  public Transaction(double amount) {
    this.amount = amount;
  }

  public double getAmount() {
    return amount;
  }
}

class InsufficientFundsException extends Exception {
  public InsufficientFundsException(String message) {
    super(message);
  }
}

public class ThreadedBank {

  public static void main(String[] args) {
    BankAccount account = new BankAccount(100); 
    final Object lock = new Object(); 
    Runnable depositTask = () -> {
      for (int i = 0; i < 10; i++) {
        try {
          synchronized (lock) { 
            account.deposit(8);
          }
          Thread.sleep(100); 
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    };

    Runnable withdrawalTask = () -> {
      for (int i = 0; i < 10; i++) {
        try {
          synchronized (lock) { 
            account.withdraw(10);
          }
          Thread.sleep(100); // Simulate some processing time
        } catch (InsufficientFundsException e) {
          System.err.println(e.getMessage());
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    };

    Thread thread1 = new Thread(depositTask);
    Thread thread2 = new Thread(withdrawalTask);

    thread1.start();
    thread2.start();
  }
}
