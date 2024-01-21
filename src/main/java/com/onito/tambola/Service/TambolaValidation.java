package com.onito.tambola.Service;

import com.onito.tambola.CustomException.TambolaValidationException;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.HashSet;

@Service
public class TambolaValidation {

    boolean validateTambolaSet(int[][][] tambolamTickets) {
        validateDuplicateInAllSet(tambolamTickets);
        for(int i=0;i<6;i++)
        {
            validateTambolaTicket(tambolamTickets[i]);
        }
        return true;
    }
    boolean validateDuplicateInAllSet(int[][][] tambolamTickets) {
        HashSet<Integer> num= new HashSet<>();
        for(int set=0;set<6;set++)
        {
            for(int row=0; row<3; row++)
            {
                for(int col=0;col<9 && tambolamTickets[set][row][col]!=0;col++)
                {
                    if(num.contains(tambolamTickets[set][row][col]))
                    {
                        throw new TambolaValidationException("Numbers are repeating in Tambola set: "+array3dToString(tambolamTickets));
                    }
                    num.add(tambolamTickets[set][row][col]);
                }
            }
        }
        return true;
    }
    private boolean validateTambolaTicket(int[][] tambolamTicket) {

        return validateAscCol(tambolamTicket) &&
                validateNumberPosition(tambolamTicket) && validateMinRowCount(tambolamTicket)&&
                validateMinColCount(tambolamTicket) ;

    }
    boolean validateMinRowCount(int[][] tambolamTicket){
        for(int row=0;row<3;row++)
        {
            if(Arrays.stream(tambolamTicket[row]).filter(num->num>0).count()!=5l)
            {
                throw new TambolaValidationException("Tambola should have exactly 5 numbers per row : "+Arrays.deepToString(tambolamTicket));
            }
        }
        return true;
    }
    boolean validateMinColCount(int[][] tambolamTicket){
        for(int col=0;col<9;col++)
        {
            int colData=0;
            for (int row = 0; row < 3; row++)
            {
                if(tambolamTicket[row][col]>0)
                {
                    colData++;
                    break;
                }
            }
            if(colData==0)
            {
                throw new TambolaValidationException("Tambola should have at least one number per column : "+Arrays.deepToString(tambolamTicket));
            }
        }
        return true;
    }
    boolean validateNumberPosition(int[][] tambolamTicket){
        for(int col=0;col<9 ;col++)
        {
            for (int row = 0; row < 3 ; row++)
            {
                if(tambolamTicket[row][col] !=0 && !(tambolamTicket[row][col]>=col*10+(col==0?1:0) &&tambolamTicket[row][col]<=col*10+9+(col==9-1?1:0)))
                {
                    throw new TambolaValidationException("Tambola values are not in respective column : "+Arrays.deepToString(tambolamTicket));
                }
            }
        }
        return true;
    }
    boolean validateAscCol(int[][] tambolamTicket){
        for(int col=0;col<9 ;col++)
        {
            int max=0;
            for (int row = 0; row < 3 ; row++)
            {
                if(tambolamTicket[row][col]!=0 && !(tambolamTicket[row][col] >= max))
                {
                    throw new TambolaValidationException("Tambola column is not in ascending order for ticket : "+Arrays.deepToString(tambolamTicket));
                }
                max=tambolamTicket[row][col];

            }
        }
        return true;
    }
    String array3dToString(int [][][] ticket)
    {
        String log="";
        for(int i=0;i<6;i++)
        {
            String arrayData="";
            for(int j=0;j<3;j++)
            {
                arrayData += Arrays.toString(ticket[i][j])+"\n";
            }
            log+=arrayData+"\n";
        }
        return log;
    }
}
