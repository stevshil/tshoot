<html>
<h1>Scenario 3</h1>

<p>The application is not responding and the monitoring system is showing that the application is down.  When it was deployed it started perfectly, but after a nightly reboot of the system for maintenance the application has not come back on line.  The system is up.

<p>The monitoring is showing;
  <ul>
    <li>The application is listening, but not running
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
