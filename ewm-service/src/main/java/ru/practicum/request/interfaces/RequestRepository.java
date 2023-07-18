package ru.practicum.request.interfaces;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.user.model.User;

import java.util.List;


@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findByIdIn(List<Long> ids);

    List<Request> findAllByEventId(Long eventId);

    Long countByEventIdAndStatus(Long eventId, RequestStatus status);

    List<Request> findByRequester(User requester);


}
