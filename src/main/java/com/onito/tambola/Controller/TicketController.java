package com.onito.tambola.Controller;

import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/ticket")
public class TicketController {
    @ResponseBody
    @GetMapping("create/{N}")
    public int[][][] create(@PathVariable int N)
    {
        for(int i=0;i<N;i++)
        {
            LinkedList<LinkedList<Integer>> trackNum=generate90Nums();

            try {
                return generate6(trackNum);
            }
            catch (Exception e)
            {
                System.out.println(e);
                e.printStackTrace();
            }

        }
        return null;

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

    int[][][] generate6(LinkedList<LinkedList<Integer>> trackNum)
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
            for(int j=0;j<3;j++)
            {
                System.out.println(Arrays.toString(ticket[i][j]));
            }
            System.out.println();
            System.out.println();
        }
        return ticket;
    }


}
