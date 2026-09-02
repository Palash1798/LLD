package com.airline.airline.repository;

import com.airline.airline.models.BoardingPass;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class BoardingPassRepository {
    private final Map<Long, BoardingPass> boardingPassTable = new TreeMap<>();
    private long previousId = 0L;

    public BoardingPass save(BoardingPass boardingPass) {
        if (boardingPass.getId() == 0) {
            previousId += 1;
            boardingPass.setId(previousId);
        }
        boardingPassTable.put(boardingPass.getId(), boardingPass);
        return boardingPass;
    }

    public Optional<BoardingPass> findByBookingId(long bookingId) {
        return boardingPassTable.values().stream()
                .filter(bp -> bp.getBookingId() == bookingId)
                .findFirst();
    }
}
