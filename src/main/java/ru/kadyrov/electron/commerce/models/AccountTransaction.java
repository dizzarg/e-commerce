package ru.kadyrov.electron.commerce.models;

import java.util.Objects;

public class AccountTransaction {

    public static enum State {
        NOT_SUBMITTED, AUTHORIZED, DECLINED, AUTHORIZATION_FAILED, IN_SETTLEMENT, SETTLED, SETTLEMENT_FAILED, VOIDED
    }

    private State state;
    private String authCode;
    private String responseCode;

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountTransaction that = (AccountTransaction) o;
        return state == that.state &&
                Objects.equals(authCode, that.authCode) &&
                Objects.equals(responseCode, that.responseCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, authCode, responseCode);
    }

    @Override
    public String toString() {
        return "AccountTransaction{" +
                "state=" + state +
                ", authCode='" + authCode + '\'' +
                ", responseCode='" + responseCode + '\'' +
                '}';
    }
}
