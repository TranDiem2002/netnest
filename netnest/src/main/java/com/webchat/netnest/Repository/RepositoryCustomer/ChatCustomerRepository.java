package com.webchat.netnest.Repository.RepositoryCustomer;

public interface ChatCustomerRepository {

    int findChatId( int userId1, int userId2);
}
