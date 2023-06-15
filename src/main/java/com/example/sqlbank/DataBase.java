package com.example.sqlbank;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class DataBase {
    String name, user, password;
    static Connection connection = null;

    public DataBase(String name, String login, String password) throws SQLException {
        this.name = name;
        this.user = login;
        this.password = password;
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + name, login, password);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public String getCardType(int id) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT name FROM card_types where id = " + id + ";");
            if (resultSet.next()) {
                return resultSet.getString("name");
            } else return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getCardPayment(int id) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT name FROM payment_methods where id = " + id + ";");
            if (resultSet.next()) {
                return resultSet.getString("name");
            } else return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String calculateSHA256(String input) {
        try {

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getName(int id) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT first_name, last_name FROM clients where id = " + id + ";");

            if (resultSet.next()) {
                return (resultSet.getString("first_name") + " " + resultSet.getString("last_name"));
            } else return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Integer[] getAccountIDs(int id) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT get_accounts(" + id + ");");
            if (resultSet.next()){
                Array array = resultSet.getArray("get_accounts");


                if (array != null && array.getArray() != null) {
                    return (Integer[]) array.getArray();
                } else return new Integer[0];
            }
            return new Integer[0];
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Integer[] getLoansIDs(int id) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT get_loans(" + id + ");");
            if (resultSet.next()) {
                Array array = resultSet.getArray("get_loans");
                if (array != null && array.getArray() != null) {
                    return (Integer[]) array.getArray();
                } else return new Integer[0];
            }
            return new Integer[0];
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addNewClient(String name, String surname, String secondName, String email, String password, String phone, String address, String corrAccount, String passport, String birthDate)
    throws Exception {
        try {
            Statement statement = connection.createStatement();
            statement.executeQuery("SELECT create_client_with_passport('" + name + "', '" + surname + "', '" + secondName + "', '" + phone + "', '" + email + "', '"
                    + calculateSHA256(password) + "', '" + passport + "', '" + address + "', '" + corrAccount + "', '" + birthDate + "');");
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getLocalizedMessage());
        }
    }

    public Integer[] getDepositsIDs(int id) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT get_deposits(" + id + ");");
            if (resultSet.next()) {
                Array array = resultSet.getArray("get_deposits");
                if (array != null && array.getArray() != null) {
                    return (Integer[]) array.getArray();
                } else return new Integer[0];
            }
            return new Integer[0];
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public ResultSet getLoanInfo(int loanID) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM loans where id = " + loanID + ";");
            return resultSet;
        } catch (SQLException e) {
            return null;
        }
    }

    public ResultSet getDeposInfo(int loanID) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM deposites where id = " + loanID + ";");
            return resultSet;
        } catch (SQLException e) {
            return null;
        }
    }

    public ResultSet getAccountInfo(int accountID) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM clients where id = " + accountID + ";");
            return resultSet;
        } catch (SQLException e) {
            return null;
        }
    }

    public Integer[] getCardsIDs(int id) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT get_cards(" + id + ");");
            if (resultSet.next()){
                Array array = resultSet.getArray("get_cards");
                if (array != null && array.getArray() != null) {
                    return (Integer[]) array.getArray();
                } else return new Integer[0];
            }
            return new Integer[0];
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet getCardInfo(int cardID) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM cards where id = " + cardID + ";");
            return resultSet;
        } catch (SQLException e) {
            return null;
        }
    }



    public String getIban(int id) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT iban FROM accounts where id = " + id + ";");
            if (resultSet.next()) {
                return resultSet.getString("iban");
            } else return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public float getBalance(int id) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT get_balance(" + id + ");");
            if (resultSet.next()) {
                return resultSet.getFloat("get_balance");
            } else return 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public int getCurrencyID(int id){
        try {
           Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT currency_id FROM accounts where id = " + id + ";");
            if (resultSet.next()) {
                return resultSet.getInt("currency_id");
            } else return 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public String getCurrencyCode(int currency_id){

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT code FROM currencies where id = " + currency_id + ";");
            if (resultSet.next()) {
                return resultSet.getString("code");
            } else return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;


    }


    public void sendMoney(String fromAccountID, String toAccountID, float amount, int cur_id, String Desc, boolean isLoan) {
        try {
            System.out.println("SELECT transfer_money('" + fromAccountID + "', '" + toAccountID + "', " + amount + ", " + cur_id + ", '" + Desc + "', " + isLoan + ");");
            Statement statement = connection.createStatement();
            statement.executeQuery("SELECT transfer_money('" + fromAccountID + "', '" + toAccountID + "', " + amount + ", " + cur_id + ", '" + Desc + "', " + isLoan + ");");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Boolean getTransactionIsSuccessful(int transactionID) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT is_successful FROM transactions where id = " + transactionID + ";");
            if (resultSet.next()) {
                Boolean isSuccessful = resultSet.getBoolean("is_successful");
                if (resultSet.wasNull()) {
                    return null;
                } else return isSuccessful;
            } else return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int checkAuthorization(String user, String password) {
        password = calculateSHA256(password);
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * FROM get_user('" + user + "');");
            if (resultSet.next()) {
                String password_hash = resultSet.getString("password_hash");
                if (password_hash.equals(password)) {
                    return resultSet.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return -1;
    }


    public static ResultSet getTransactionsAccount(int accountId) throws SQLException {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " +
                    "get_transaction_ids_by_account(" + accountId + ");");
            return resultSet;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static ResultSet getTransactionsCard(int cardID) throws SQLException {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " +
                    "get_transaction_ids_by_card(" + cardID + ");");
            return resultSet;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static ResultSet getTransactionDetails(int transactionID) {
        String sql = "SELECT * FROM transactions WHERE id = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, transactionID);
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String getTransactionDescriptionById(int transactionId) throws SQLException {

        String sql = "SELECT get_transaction_description_by_id(?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, transactionId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
        }
        return null;
    }

    public int getSenderAccountByTransactionId(int transactionId) throws SQLException {
        String sql = "SELECT get_sender_account_by_transaction_id(?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, transactionId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public int getReceiverAccountByTransactionId(int transactionId) throws SQLException {
        String sql = "SELECT get_receiver_account_by_transaction_id(?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, transactionId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public String getTransactionTypeById(int typeId) throws SQLException {
        String sql = "SELECT get_transaction_type_by_id(?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, typeId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
        }
        return null;
    }

    public double getTransactionAmountByIdFrom(int transactionId) throws SQLException {
        String sql = "SELECT * from get_from_amount(?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, transactionId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        }
        return 0.0;
    }
    public double getTransactionAmountByIdTo(int transactionId) throws SQLException {
        String sql = "SELECT * from get_to_amount(?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, transactionId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        }
        return 0.0;
    }

    public Integer getSenderCardByTransactionId(int transactionId) throws SQLException {
        String sql = "SELECT get_sender_card_by_transaction_id(?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, transactionId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return null;
    }


    public static Integer getCardAccount(int cardId) throws SQLException {
        String sql = "SELECT get_linked_account_id(?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, cardId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return null;
    }

    public String getTransactionDateById(int transactionId) throws SQLException {
        String sql = "SELECT get_transaction_date_by_id(?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, transactionId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
        }
        return null;
    }



}

