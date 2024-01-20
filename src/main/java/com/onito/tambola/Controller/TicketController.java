package com.onito.tambola.Controller;

import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/ticket")
public class TicketController {
    @ResponseBody
    @GetMapping("create/{N}")
    public ArrayList<String> create(@PathVariable int N)
    {
        ArrayList<String> al = new ArrayList<>();
        for(int i=0;i<N;i++)
        {
            LinkedList<LinkedList<Integer>> trackNum=generate90Nums();
            for(int j=0;j<6;j++)
            {
                try {
                    al.add(generate6(trackNum));
                }
                catch (Exception e)
                {
                    System.out.println(e);
                    e.printStackTrace();
                }
            }
        }
        return al;

    }

    private LinkedList<LinkedList<Integer>> generate90Nums() {
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
    private LinkedList<LinkedList<Integer>> generateCols() {
        LinkedList<LinkedList<Integer>> trackCol= new LinkedList<>();

        for(int i=0;i<3;i++)
        {
            LinkedList<Integer> nums=new LinkedList<>();
            for(int j=0;j<9;j++)
            {
                nums.add(j);
            }

            trackCol.add(nums);
        }
        return trackCol;
    }

    String generate6(LinkedList<LinkedList<Integer>> trackNum)
    {
        LinkedList<LinkedList<Integer>> trackCol=generateCols();
        Random random=new Random();
        int[][] ticket = new int[3][9];
        int rowCount[]=new int[3];
        //making all columns have at least one val
        for(int col=0;col<9;col++)
        {
            int row = random.nextInt(3);
            while(rowCount[row]==5)
            {
                row = random.nextInt(3);
            }
            if(trackNum.get(col).size()>0)
            {
                Collections.shuffle(trackNum.get(col));
                ticket[row][col]=trackNum.get(col).remove(0);
                trackCol.get(row).remove((Object)col);
                rowCount[row]++;// remembering which row has how many values.(exactly 5 we need in every row)
            }
        }
        for(int row=0;row<3;row++)
        {
            while(rowCount[row]<5 && trackCol.get(row).size()>0)
            {
                Collections.shuffle(trackCol.get(row));
                int col=trackCol.get(row).remove(0);
                if(trackNum.get(col).size()>0)
                {
                    Collections.shuffle(trackNum.get(col));
                    ticket[row][col]=trackNum.get(col).remove(0);
                    trackCol.get(row).remove((Object)col);
                    rowCount[row]++;// remembering which row has how many values.(exactly 5 we need in every row)
                }
            }
        }

        for(int col=0;col<9;col++)
        {
            int toSort[]=new int[3];
            for(int row=0;row<3;row++)
            {
                toSort[row]=ticket[row][col];
            }
            Arrays.sort(toSort);
            for(int row=0,i=0;row<3;row++)
            {
                if(ticket[row][col] != 0) {
                    while (i < 3 && toSort[i] == 0 && ++i > 0) {}
                    ticket[row][col] = toSort[i++];
                }
            }
        }
        return String.format("%s\n%s\n%s\n",Arrays.toString(ticket[0]),Arrays.toString(ticket[1]),Arrays.toString(ticket[2]));
    }

}
