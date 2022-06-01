import java.io.*;
import java.net.InetAddress;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        HashMap<Integer, ArrayList<String>> clock_map = new HashMap<>();

        if (args.length == 0) {
            System.out.println("Usage: java Main (number of processses) [filename of commands]");
            return;
        }

        String input;
        Random r = new Random();

        try {
            int n = Integer.parseInt(args[0]);
            LamportClock[] clocks = new LamportClock[n];
            System.setProperty("java.net.preferIPv4Stack" , "true");
            InetAddress group = InetAddress.getByName("224.255.255.255");
            for (int i = 0; i < n; ++i) {
                int port = 8888;
                LamportClock lc = new LamportClock(group, port, i);
                lc.start();
                clocks[i] = lc;
            }

            Thread[] threads = new Thread[n];
            int[] desl = new int[]{2,4,20,5};

            for(int i=0;i < n;i++){
                final int g = i;

                threads[i] = new Thread(()->{
                    int requests=0;
                    if(g%2!=0){
                        //impar
                        for(int k=0;k < 13;k++){
                            try { Thread.sleep(desl[g]*500);} catch (InterruptedException e) {e.printStackTrace();}
                            if(r.nextInt(2) == 0 && requests<3){
                                // requests
                                long firstProcessId = clocks[g].getId();
                                int secondProcessId = -1;
                                String messageContent = "";
                                Event e = new Event(3, firstProcessId, secondProcessId, messageContent);
                                try {
                                    clocks[g].updateTime(e);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                requests++;

                                if(!clock_map.containsKey((int)firstProcessId))
                                    clock_map.put((int) firstProcessId,new ArrayList<>());
                                clock_map.get((int)firstProcessId).add("request at "+firstProcessId+" message "+messageContent+" at "+clocks[g].getNanoTime());
                            }else{
                                long firstProcessId = clocks[g].getId();
                                int secondProcessId = 0;
                                String messageContent = "";

                                Event e = new Event(0, firstProcessId, secondProcessId, messageContent);
                                try {
                                    clocks[g].updateTime(e);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }

                                if(!clock_map.containsKey((int)firstProcessId))
                                    clock_map.put((int) firstProcessId,new ArrayList<>());
                                clock_map.get((int)firstProcessId).add("local at "+firstProcessId+" message "+messageContent+" at "+clocks[g].getNanoTime());
                            }
                        }
                    }else{
                        //par
                        for(int k=0;k < 28;k++){
                            try { Thread.sleep(desl[g]*500);} catch (InterruptedException e) {e.printStackTrace();}
                            if(r.nextInt(2) == 0 && requests<8){
                                // requests
                                long firstProcessId = clocks[g].getId();
                                int secondProcessId = -1;
                                String messageContent = "";
                                Event e = new Event(3, clocks[g].getId(), -1, "");
                                try {
                                    clocks[g].updateTime(e);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                requests++;

                                if(!clock_map.containsKey((int)firstProcessId))
                                    clock_map.put((int) firstProcessId,new ArrayList<>());
                                clock_map.get((int)firstProcessId).add("request at "+firstProcessId+" message "+messageContent+" at "+clocks[g].getNanoTime());
                            }else{
                                long firstProcessId = clocks[g].getId();
                                int secondProcessId = 0;
                                String messageContent = "";
                                Event e = new Event(0, clocks[g].getId(), 0, "");
                                try {
                                    clocks[g].updateTime(e);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                if(!clock_map.containsKey((int)firstProcessId))
                                    clock_map.put((int) firstProcessId,new ArrayList<>());
                                clock_map.get((int)firstProcessId).add("local at "+firstProcessId+" message "+messageContent+" at "+clocks[g].getNanoTime());
                            }
                        }
                    }
                });
            }

            Arrays.stream(threads).forEach(Thread::start);

            Arrays.stream(threads).forEach((x)->{
                try { x.join();} catch (InterruptedException e) {e.printStackTrace();}
            });

            File ff = new File("output.out");
            try {
                FileWriter ffw = new FileWriter(ff);
                ffw.write("");

                clock_map.forEach( (x,y) -> {
                    System.out.println("Processo "+x+":");
                    try {
                        ffw.append("Processo "+x+":\n");
                    } catch (IOException e) {e.printStackTrace();}

                    y.stream().forEach( (z) -> {
                        System.out.println("    "+z);
                        try {
                            ffw.append("    "+z+"\n");
                        } catch (IOException e) {e.printStackTrace();}
                    });
                });

                ffw.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            Arrays.stream(clocks).forEach(Thread::interrupt);

            System.exit(0);

            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                input = in.readLine();
                if (input.equals("exit"))
                    return;

                /**
                 * A message format is of the following:
                 * EVENT_NAME ID_OF_SENDER (ID_OF_RECEIVER)
                 * 
                 * EVENT_NAME is of the following:
                 *  - SEND
                 *  - LOCAL
                 * 
                 * For example:
                 * SEND 1 2 (process 1 sends a message to process 2)
                 * LOCAL 3 (process 3 performs a local event)
                 */
                // perform a string split operation based on space
                String[] splits = input.split(" ");
                if (splits.length == 0) {
                    continue;
                }

                switch(splits[0].toUpperCase()) {

                    case "SEND":
                        int clockArrayId = Integer.parseInt(splits[1]);
                        long firstProcessId = clocks[clockArrayId].getId();
                        long secondProcessId = clocks[Integer.parseInt(splits[2])].getId();
                        String messageContent = "";
                        if (splits.length >= 3) {
                            List<String> wordsList = Arrays.asList(
                                Arrays.copyOfRange(splits, 3, splits.length));
                            messageContent = String.join(" ", wordsList);
                        }

                        Event e = new Event(1, firstProcessId, secondProcessId, messageContent);
                        clocks[clockArrayId].updateTime(e);
                        break;

                    case "LOCAL":
                        clockArrayId = Integer.parseInt(splits[1]);
                        firstProcessId = clocks[clockArrayId].getId();
                        secondProcessId = 0;
                        messageContent = "";

                        e = new Event(0, firstProcessId, secondProcessId, messageContent);
                        clocks[clockArrayId].updateTime(e);
                        break;

                    case "REQUEST":
                        clockArrayId = Integer.parseInt(splits[1]);
                        firstProcessId = clocks[clockArrayId].getId();
                        e = new Event(3, firstProcessId, -1, "");
                        clocks[clockArrayId].updateTime(e);
                        break;

                    default:
                        throw new RuntimeException("Invalid event name");

                }
            }

        } catch(Exception e) {
            System.err.println(e);
            return;
        }
    }

}