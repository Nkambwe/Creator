package com.pbu.agents.services;

import com.pbu.agents.requests.BankRequest;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BankServiceImp implements BankService {
    @Override
    public CompletableFuture<Boolean> bankExists(long id) {
        return null;
    }

    @Override
    public boolean checkBankDuplicateName(String name) {
        return false;
    }

    @Override
    public boolean checkBankDuplicateNameWithDifferentIds(String name, long id) {
        return false;
    }

    @Override
    public boolean checkBankDuplicateSortCode(String sort_code) {
        return false;
    }

    @Override
    public boolean checkBankDuplicateSortCodeWithDifferentIds(String sort_code, long id) {
        return false;
    }

    @Override
    public CompletableFuture<BankRequest> findBankById(long id) {
        return null;
    }

    @Override
    public CompletableFuture<BankRequest> findBankBySortCode(String sort_code) {
        return null;
    }

    @Override
    public CompletableFuture<List<BankRequest>> getBanks() {
        return null;
    }

    @Override
    public CompletableFuture<List<BankRequest>> getActiveBanks() {
        return null;
    }

    @Override
    public CompletableFuture<BankRequest> createBank(BankRequest bank) throws InterruptedException {
        return null;
    }

    @Override
    public void updateBank(BankRequest bank) {

    }

    @Override
    public void softDeleteBank(long id, boolean is_deleted) {

    }

    @Override
    public void deleteBank(long id) {

    }
}
