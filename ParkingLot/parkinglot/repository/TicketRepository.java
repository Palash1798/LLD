package com.palash.parkinglot.repository;

import com.palash.parkinglot.models.Gate;
import com.palash.parkinglot.models.Ticket;

import java.sql.PreparedStatement;
import java.util.Map;
import java.util.TreeMap;

public class TicketRepository {
    private Map<Long, Ticket> ticketTable = new TreeMap<>();

    // static counter to generate id.
    // will this work for distributed system? use UUID there, or other algorithms.

    private Long previousId = 0L;
    public Ticket saveTicket(Ticket ticket){
        // db.execute(insert into ticket(...) values (ticket.getEntryTime()...));
        // String response = db.execute(select * from tickets_entry_time = ticket.getEntryDate());
        // ticket = convertToTicketObject(response);
        previousId += 1;
        ticket.setId(previousId);
        ticketTable.put(previousId, ticket);
        return ticket;
    }
}
