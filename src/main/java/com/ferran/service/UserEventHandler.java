package com.ferran.service;


import com.ferran.repository.SessionInMemoryRepository;

public class UserEventHandler implements EventHandler<UserEvent>{

    SessionInMemoryRepository sessionRepository;

    public UserEventHandler(SessionInMemoryRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void process(UserEvent event) {
        sessionRepository.findByUser(event.getUser()).ifPresent(
                s ->{
                    switch (event.getAction()){
                        case UPDATE:
                            s.setUser(event.getUser());
                            break;
                        case DELETE:
                            sessionRepository.remove(s.getToken());
                    }
                }
        );
    }
}
