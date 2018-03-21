#!/bin/bash

# Background task that duplicates trades in database
(
mysql -h mysql.server -u trades -ptrades trades <<'_END_'
SET FOREIGN_KEY_CHECKS=0;
DROP INDEX transid ON Trades;
DROP INDEX stock ON Trades;
_END_

sleep 45

while :
do
  if (( $(mysql -h mysql.server -u trades -ptrades trades -e "select count(transid) from Trades WHERE id=(SELECT max(id) from Trades);" | tail -1) < 2 ))
  then
  mysql -vvv -h mysql.server -u trades -ptrades trades <<'_END_'
  -- SET FOREIGN_KEY_CHECKS=0;
  set @maxid=(select max(id) from Trades);
  INSERT INTO Trades (transid,stock,ptime,price,volume,buysell,state,stime)
	SELECT transid,stock,ptime,price,volume,buysell,state,stime FROM Trades WHERE id=@maxid;
	-- SET FOREIGN_KEY_CHECKS=1;
_END_
  fi

  sleep $(($RANDOM%80))
done
) >/var/log/watcher 2>&1 &
