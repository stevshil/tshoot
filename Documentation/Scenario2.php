<html>
<h1>Scenario 2</h1>

<p>Users are not able to see the application.  When trying to access the web application they are receiving <b>"the page is not working"</b>.

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
