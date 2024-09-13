package multithreading;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class ExecuterServiceExamples {

    private  static  Runnable newRunnable(String msg){
        return new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " "+msg);
            }
        };
    }

    // Callable is used here since the run method of Runnable is void so it does not returns anything while the Callable run method returns a Object
    private static Callable newCallable(String msg){
        return new Callable() {
            @Override
            public Object call() throws Exception {
                String completeMsg = Thread.currentThread().getName() + " " + msg;
                return completeMsg;
            }
        };
    }

    private static void SimplePoolCreation(){
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        executorService.execute(newRunnable("task 1"));
        executorService.execute(newRunnable("task 2"));
        executorService.execute(newRunnable("task 3"));
        executorService.shutdown();
    }

    private static  void poolCreationWithThreadPoolExecutor(){
        int corePoolSize = 3;
        int maxPoolSize = 3;
        long keepAliveTime = 3000;

        ExecutorService executorService1 = new ThreadPoolExecutor(corePoolSize,maxPoolSize,keepAliveTime, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<>(128));

        executorService1.execute(newRunnable("task 1"));
        executorService1.execute(newRunnable("task 2"));
        executorService1.execute(newRunnable("task 3"));
        executorService1.shutdown();
    }

    private static void threadWithFuture(){
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        // with Callable
//        Future future = executorService.submit(newCallable("task 1"));
//        try{
//            String msg = (String) future.get();
//            System.out.println("msg from callable method :"+msg);
//        }catch (InterruptedException e){
//        }catch (ExecutionException ex){
//        }
//        System.out.println("is future done."+future.isDone());

        List<Callable<String>> callableList = new ArrayList<>();
        callableList.add(newCallable("task1"));
        callableList.add(newCallable("task2"));
        callableList.add(newCallable("task3"));

        // Invoke Any : this method stops the execution of other threads when any one of the thread is completed
        // this is used for the cases where we want the threads to invoke multiple same servers and whichever server responds first we can cancel the results for later threads
        // reducing the latency of the server call
//        try{
//             String msg =(String) executorService.invokeAny((Collection)callableList);
//             System.out.println(msg);
//        }catch (InterruptedException e){}
//        catch (ExecutionException ex){}


        // Invoke All:

        try{
            List<Future<String>> futures = executorService.invokeAll((Collection<Callable<String>>) callableList);
            for(Future<String> future:futures){
                String msg  = future.get();
                System.out.println(msg);
            }
        }catch (InterruptedException e){}
        catch (ExecutionException ex){}


        executorService.shutdown();
    }


    public static void main(String[] args) {

        // example 1 : simple flow of executer service
//        SimplePoolCreation();
        // example 2 : with ThreadPoolExecutor
//        poolCreationWithThreadPoolExecutor();

        // Executor with Future
        threadWithFuture();

        // with callable instead of Runnable



    }

}
