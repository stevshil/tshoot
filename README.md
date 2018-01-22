# Trouble shooting system

## Brief

This project is an online testing system that enables people to trouble shoot predefined scenarios and fix them.

It makes use of Docker images behind the scenes which the students will log on to and attempt to solve the issues.  When complete they will run a program that will check their work and store the relevant information in an admin area for someone to review as well as providing a score.

On the student side it will show progress of their work, e.g. started, submitted, yet to take.

The launching and admin is performed through the Web UI which communicates with the Docker backend to start containers which will have port forwarding configured for the students.  The ports for SSH and Web will appear in the web UI for the student to enable connection.

## Web front end

Written in PHP passing docker commands to the backend.

# Vagrant TShoot

This directory contains a Vagrant configuration that builds a demo VM that can
* build the docker images
* store the images in a docker registry
* run the web server front end

The VM will act like the final server that the software would be installed on

# Useful files

The student will only be able to log on to the application server.

From this server we need to capture all Linux commands (use script) and all MySQL commands which can be obtained from the .mysql_history file in the users home directory.

To capture the users commands we will launch the script program through exec, so that if they log off it will log them off rather than back to a shell.

exec script .$(hostname)_$(date +'%Y%m%d-%H%M')

This will geneate a different file for each time the student logs on
