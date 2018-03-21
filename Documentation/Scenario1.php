<html>
<h1>Scenario 1</h1>

<h2>Pre-requisite</h2>
<p>First you should check out what the application looks like and how it runs before attempting this scenario and any others.  You should also read the documentation about the application too.

<h2>Overview</h2>
<p>In this scenario the users of the system are complaining that there are no updates occurring in the application.  No trades are appearing, or updates to trades.

<p>Our monitoring systems are showing that all systems are up and operational;
  <ul>
    <li>Database
    <li>ActiveMQ
    <li>Applicaton
  </ul>

<p>You will need to log on to the application server and work out what the issue is from manual investigation, as our monitoring system is not detecting any issues with this application.  When you find out what the issue is you should make a note of what the issue was and provide a solution as to how to monitor this problem next time.

<p>You should log on to the server and start your investigation;
<p><code>ssh -p <b>SSH</b> student@<b>SSHIP</b></code>
<p>e.g. ssh -p 1030 student@<?php echo $_SERVER['SERVER_NAME']; ?>

<p>Where;
<ul>
<li><b>SSH</b> = the port number shown to you for SSH
<li><b>SSHIP</b> is the IP address or hostname to ssh to
</ul>
</html>
