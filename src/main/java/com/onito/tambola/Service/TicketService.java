package com.onito.tambola.Service;

import com.onito.tambola.CustomException.TicketIdNotFoundException;
import com.onito.tambola.Entity.Ticket;
import com.onito.tambola.Repository.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;


@Service
public class TicketService {
    private static final Logger logger = LoggerFactory.getLogger(TicketService.class);

    @Autowired
    private TicketRepository ticketRepository;

    public Page<Ticket> getAllTickets(Pageable pageable) {
        return ticketRepository.findAll(pageable);
    }
    public LinkedList<LinkedList<Integer>> generate90Nums() {
        LinkedList<LinkedList<Integer>> trackNum= new LinkedList<>();

        for(int i=0;i<9;i++)
        {
            LinkedList<Integer> nums=new LinkedList<>();
            for(int j=0;j<10;j++)
            {
                nums.add(i*10+j);
            }
            if(i==0)
            {
                nums.remove(0);
            }
            else if(i==9-1)
            {
                nums.add(9*10);
            }
            trackNum.add(nums);
        }
        return trackNum;
    }
    private LinkedList<LinkedList<LinkedList<Integer>>> generateCols() {
        LinkedList<LinkedList<LinkedList<Integer>>> trackCol= new LinkedList<>();
        for(int k=0;k<6;k++)
        {
            LinkedList<LinkedList<Integer>> twoD = new LinkedList<>();
            for (int i = 0; i < 3; i++)
            {
                LinkedList<Integer> oneD = new LinkedList<>();
                for (int j = 0; j < 9; j++)
                {
                    oneD.add(j);
                }
                twoD.add(oneD);
            }
            trackCol.add(twoD);
        }
        return trackCol;
    }

    public int[][][] generate6Tickets(LinkedList<LinkedList<Integer>> trackNum)
    {
        LinkedList<LinkedList<LinkedList<Integer>>> trackCol=generateCols();
        Random random=new Random();
        int[][][] ticket = new int[6][3][9];
        int rowCount[][]=new int[6][3];
        //making all columns have at least one val
        for(int i=0;i<6;i++)
        {
            for (int col = 0; col < 9; col++)
            {
                int row = random.nextInt(3);
                while (rowCount[i][row] == 5)
                {
                    row = random.nextInt(3);
                }
                if (trackNum.get(col).size() > 0)
                {
                    Collections.shuffle(trackNum.get(col));
                    ticket[i][row][col] = trackNum.get(col).remove(0);
                    trackCol.get(i).get(row).remove((Object) col);
                    rowCount[i][row]++;// remembering which row has how many values.(exactly 5 we need in every row)
                }
            }
        }
        for(int i=0;i<6;i++)
        {
            for (int row = 0; row < 3; row++)
            {
                while (rowCount[i][row] < 5 && trackCol.get(i).get(row).size() > 0)
                {
                    Collections.shuffle(trackCol.get(i).get(row));
                    int col = trackCol.get(i).get(row).remove(0);
                    if (trackNum.get(col).size() > 0)
                    {
                        Collections.shuffle(trackNum.get(col));
                        ticket[i][row][col] = trackNum.get(col).remove(0);
                        trackCol.get(row).remove((Object) col);
                        rowCount[i][row]++;// remembering which row has how many values.(exactly 5 we need in every row)
                    }
                }
            }
        }
        for(int i=0;i<6;i++)
        {
            for(int col=0;col<9;col++)
            {
                int toSort[]=new int[3];
                for(int row=0;row<3;row++)
                {
                    toSort[row]=ticket[i][row][col];
                }
                Arrays.sort(toSort);
                for(int row=0,h=0;row<3;row++)
                {
                    if(ticket[i][row][col] != 0) {
                        while (h < 3 && toSort[h] == 0 && ++h > 0) {}
                        ticket[i][row][col] = toSort[h++];
                    }
                }
            }
        }
        for(int i=0;i<6;i++)
        {
            logger.info("ticket hash : "+getArrayHash(ticket[i]));
            String arrayData="";
            for(int j=0;j<3;j++)
            {
                arrayData += Arrays.toString(ticket[i][j])+"\n";
            }
            logger.info(arrayData+"\n");
        }
        return ticket;
    }
    public String getArrayHash(int[][] array) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] arrayBytes = Arrays.deepToString(array).getBytes();
            byte[] hashBytes = md.digest(arrayBytes);
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            logger.error("NoSuchAlgorithmException for SHA-256");
            e.printStackTrace();
            return null;
        }
    }

    public boolean existsByArrayHashIn(ArrayList<String> ticketHashes) {
        return ticketRepository.existsByArrayHashIn(ticketHashes);
    }

    public List<Ticket> saveAll(List<Ticket> ticketsToSave) {
        return ticketRepository.saveAll(ticketsToSave);
    }

    public Ticket findById(Long id) {
        Optional<Ticket> opTicket = ticketRepository.findById(id);
        if(opTicket.isPresent())
        {
            return opTicket.get();
        }
        throw new TicketIdNotFoundException(String.format("Ticket id:%d is not present in database",id));
    }

    public List<Ticket> findByGroupId(UUID groupId) {
        return ticketRepository.findByGroupId(groupId);
    }

    public HashMap<String,int[][]> createNSets(int N) {
        List<Ticket> ticketsToSave = new ArrayList<>();
        HashMap<String,int[][]> responseMap= new HashMap<>();
        for(int i=0;i<N;i++)
        {
            LinkedList<LinkedList<Integer>> trackNum = generate90Nums();
            int tambolamTickets[][][] = generate6Tickets(trackNum);
            ArrayList<String> ticketHashes = new ArrayList<>();
            for(int j=0;j<6;j++)
            {
                ticketHashes.add(getArrayHash(tambolamTickets[j]));
            }
            boolean anyExist = existsByArrayHashIn(ticketHashes);
            if (anyExist)
            {//ticket already exists so regenerating tickets
                logger.warn("ticket already exists so regenerating tickets" + ticketHashes);
                i--;
                continue;
            }
            else
            {
                UUID groupId = UUID.randomUUID();
                for(int j=0;j<6;j++)
                {
                    Ticket ticket=new Ticket(0l, groupId, LocalDateTime.now(), tambolamTickets[j], ticketHashes.get(j));
                    ticketsToSave.add(ticket);
                }
            }
        }
        try {
            ticketsToSave = saveAll(ticketsToSave);
        }catch (DataIntegrityViolationException e)
        {
            logger.error("Duplicate ticket already exists in same Tambola set" );
            throw e;
        }
        for (Ticket t: ticketsToSave) {
            responseMap.put(""+t.getId(),t.getTicket());
        }
        return responseMap;
    }
}