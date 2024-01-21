package com.onito.tambola.Controller;

import com.onito.tambola.Entity.Ticket;
import com.onito.tambola.Service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;


@RestController
@RequestMapping("/ticket")
public class TicketController {
    @Autowired
    private TicketService ticketService;
    @GetMapping("/{id}")
    public int[][] findById(@PathVariable Long id)
    {
        return ticketService.findById(id).getTicket();
    }
    @GetMapping("groupByTicketId/{id}")
    public ResponseEntity<List<Ticket>> findGroupByTicketId(@PathVariable Long id)
    {
        UUID groupId = ticketService.findById(id).getGroupId();
        return new ResponseEntity<>(ticketService.findByGroupId(groupId),HttpStatus.OK);
    }
    @GetMapping("/all")
    public ResponseEntity<Page<Ticket>> fetchAll(@RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "12") int size)
    {

        return new ResponseEntity<>(ticketService.getAllTickets(PageRequest.of(page, size)), HttpStatus.OK);
    }
    @ResponseBody
    @GetMapping("create/{N}")
    public HashMap<String,int[][]> create(@PathVariable int N)
    {
        return ticketService.createNSets(N);
    }
}
