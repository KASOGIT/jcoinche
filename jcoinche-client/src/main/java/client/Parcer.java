package client;

import protomsg.CoincheMsg;

import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by kj on 26/11/16.
 */
public class Parcer {
    private static String send;
    private static String[] tabSend;
    private Scanner     scan = new Scanner(System.in);

    private static Map<String, Integer> mapCard = new HashMap<>();

        static {
            mapCard.put("HEARTS", 1);
            mapCard.put("DIAMOND", 2);
            mapCard.put("SPADES", 3);
            mapCard.put("CLUB", 4);
            mapCard.put("7", 7);
            mapCard.put("8", 8);
            mapCard.put("9", 9);
            mapCard.put("10", 10);
            mapCard.put("JACK", 11);
            mapCard.put("QUEEN", 12);
            mapCard.put("KING", 13);
            mapCard.put("ACE", 14);
            mapCard.put("NONE", 0);
    }



    public void    readLine() {
        send = scan.nextLine();
        send = send.replaceAll("\t", " ");
        send = send.trim().replaceAll(" +", " ");
        tabSend = send.split(" ");
    }

    public CoincheMsg.ClientMsg readAnnounce() {
        readLine();
        if (tabSend[0].equals("PASS")) {
            CoincheMsg.ClientMsg msgSend = CoincheMsg.ClientMsg.newBuilder()
                    .setTypeRequest(CoincheMsg.ClientMsg.TypeRequestClient.ANNOUCE)
                    .setAnnounce(CoincheMsg.ClientMsg.Annouce.newBuilder()
                            .setValue(-1)
                            .setTrump(-1)
                            .setPass(1)
                    ).build();
            return (msgSend);
        }
        else if (tabSend[0].equals("ANNOUNCE")) {
            if (tabSend[1].isEmpty() || tabSend[2].isEmpty()) {
                System.out.println("Error in command ANNOUNCE , please enter ANNOUNCE [value > 0] [trump]");
            }
            try {
                int value = Integer.parseInt(tabSend[1]);
                int trump = mapCard.get(tabSend[2]);

                if (value >= 80 && value <= 180 && trump >= 0)
                    return (createAnnounce(value, trump));
            }
            catch (Exception e) {
                System.out.println("Error command please enter (PASS or ANNOUNCE [value > 0] [trump]");
                return (readAnnounce());
            }
        }
        System.out.println("Error command please enter (PASS or ANNOUNCE [value > 0] [trump]");
        return (readAnnounce());
    }

    private CoincheMsg.ClientMsg createAnnounce(int value, int trump) {
        CoincheMsg.ClientMsg msgSend = CoincheMsg.ClientMsg.newBuilder()
                .setTypeRequest(CoincheMsg.ClientMsg.TypeRequestClient.ANNOUCE)
                .setAnnounce(CoincheMsg.ClientMsg.Annouce.newBuilder()
                        .setValue(value)
                        .setTrump(trump)
                        .setPass(10)
                ).build();
        return (msgSend);
    }

    public  int    readPlay()
    {
        readLine();
        if (tabSend[0].equals("PLAY")) {
            if (!tabSend[1].isEmpty()) {
                try {
                    int value = Integer.parseInt(tabSend[1]);

                    if (value >= 0)
                        return value;
                } catch (Exception e) {
                    System.out.println("Error command please enter (PASS or ANNOUNCE [value > 0] [trump]");
                    return (readPlay());
                }
            }
        }
        System.out.println("Error comand please enter PLAY [Value] [Trump]");
        return (readPlay());
    }
}
