package com.onito.tambola.Repository;

import com.onito.tambola.Entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket,Long> {
    boolean existsByArrayHashIn(Collection<String> arrayHashes);

    List<Ticket> findByGroupId(UUID groupId);
}
