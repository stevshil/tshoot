<html>
<h1>Scenario 4</h1>

<p>Someone has reported in ServiceNow that some of the trades are coming in twice.  The /opt/trade-app/target/trade-record.log should show the transactions that have been happening.

<p>The developers state that the application jar has not changed.

<p>The monitoring is showing;
  <ul>
    <li>The application is running and responding
    <li>Database is online and responding
    <li>ActiveMQ is online and responding
  </ul>

<p>You should log on to the server and start your investigation;

<p><code>ssh -p <b>SSH</b> student@<b>SSHIP</b>
<p>e.g. <code>ssh -p 1030 student@<?php echo $_SERVER['SERVER_NAME']; ?></code>

<p>Where;
<ul>
  <li><b>SSH</b> = the port number shown to you for SSH
  <li><b>SSHIP</b> = the IP address or hostname to ssh to
</html>

<p>With this fix you will need to rectify the problem and reset any values to the correct values within the trade-log.
