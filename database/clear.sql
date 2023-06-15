DROP TABLE IF EXISTS accounts,currencies,account_balance,currencies_exchange_rates,currencies_history,transaction_types,ownerships,account_types,clients,cards,card_types,transactions,loans,deposits,stocks,investments,stocks_holders,payment_methods CASCADE;
DROP FUNCTION IF EXISTS do_nothing,
cards_upd_,
transactions_upd_,
get_user,
get_accounts,
get_cards,
get_deposits,
get_loans,
find_client_by_email_or_phone,
generate_random_iban,
create_account(INT,INT,INT),
create_client_with_pesel,
create_client_with_passport,
get_exchange_rate,
get_sender_card_by_transaction_id,
find_currency,
convert_currency,
get_from_amount,
get_to_amount,
get_balance_check,
get_balance,
get_standard_daily_limit,
get_standard_monthly_limit,
get_standard_daily_limit_card,
get_standard_monthly_limit_card,
get_daily_hard_limit,
get_monthly_hard_limit,
get_daily_limit_account,
get_monthly_limit_account,
add_owner_to_account,
create_card,
get_transaction_ids_by_account,
get_transaction_description_by_id,
get_sender_account_by_transaction_id,
get_receiver_account_by_transaction_id,
get_transaction_type_by_id,
get_transaction_amount_by_id,
get_transaction_date_by_id,
update_currency_rate,
get_account_id_by_iban,
get_last_update,
get_account_currency,
get_transaction_type_id,
get_commission,
transfer_money,
get_linked_account_id,
add_card_transaction,
get_iban,
change_card_daily_limit,
change_card_monthly_limit,
change_account_daily_limit,
change_account_monthly_limit,
change_account_monthly_limit,
block_card,
block_account,
generate_transaction(INT,INT,INT,NUMERIC(13,3),VARCHAR(100),BOOLEAN),
generate_transaction(VARCHAR(34),INT,INT,NUMERIC(13,3),VARCHAR(100),BOOLEAN),
generate_transaction(INT,VARCHAR(34),INT,NUMERIC(13,3),VARCHAR(100),BOOLEAN),
create_loan,
create_deposit,
validate_pesel,
validate_email,
validate_phone,
validate_name,
validate_passport,
validate_currencies_history,
is_card_blocked,
is_account_blocked,
get_card_daily_limit,
get_card_monthly_limit,
validate_balance_before_transaction,
money_spent_from_account,
money_spent_from_card,
validate_balance_after_transaction,
normalize_,
check_card_limits;
DROP TRIGGER IF EXISTS currencies_del_upd ON currencies;
DROP TRIGGER IF EXISTS currencies_history_del_upd ON currencies_history;
DROP TRIGGER IF EXISTS account_types_del ON account_types;
DROP TRIGGER IF EXISTS clients_del ON clients;
DROP TRIGGER IF EXISTS accounts_del ON accounts;
DROP TRIGGER IF EXISTS ownerships_del_upd ON ownerships;
DROP TRIGGER IF EXISTS card_types_del_upd ON card_types;
DROP TRIGGER IF EXISTS cards_del ON cards;
DROP TRIGGER IF EXISTS cards_upd ON cards;
DROP TRIGGER IF EXISTS transaction_types_del ON transaction_types;
DROP TRIGGER IF EXISTS transactions_del ON transactions;
DROP TRIGGER IF EXISTS transactions_upd ON transactions;
DROP TRIGGER IF EXISTS trigger_validate_pesel ON clients;
DROP TRIGGER IF EXISTS trigger_validate_email ON clients;
DROP TRIGGER IF EXISTS trigger_validate_phone ON clients;
DROP TRIGGER IF EXISTS trigger_validate_name ON clients;
DROP TRIGGER IF EXISTS trigger_validate_passport ON clients;
DROP TRIGGER IF EXISTS trigger_validate_currencies_history ON currencies_history;
DROP TRIGGER IF EXISTS trigger_validate_balance_before_transaction ON accounts;
DROP TRIGGER IF EXISTS trigger_validate_balance_after_transaction ON accounts;
DROP TRIGGER IF EXISTS create_trigger_on_card ON cards;