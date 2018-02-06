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

## Useful files

The student will only be able to log on to the application server.

From this server we need to capture all Linux commands (use script) and all MySQL commands which can be obtained from the .mysql_history file in the users home directory.

To capture the users commands we will launch the script program through exec, so that if they log off it will log them off rather than back to a shell.  A further fix to this is to move it into the global profile and bashrc settings, but the user could still over ride this by adding their own profile or .bashrc.

## Captures

On completion the files are stored on the main server in /var/testresults with a directory under there based on the users name and the test.  The result is stored in a file with the number of times they ran the *completed* script (can only be run using sudo) as well as the logs for the mysql commands and shell commands with output for both.  These are hidden files and are located in /var/.user on the container while running and copied to the /var/testresults directory as a log directory when the completed script is run.

# Running the demo system

To run this system in a self-contained VirtualBox VM simply do the following in the directory where you see the Vagrantfile;

```vagrant up tshoot```

This will create the test environment on a Linux RHEL (CentOS 7) VM and pull down all the necessary Docker images and build the application container.

All images will be saved to a local Docker registry which the VM can call through localhost:5000.  If you destroy the VM these images will still be located on your local disk in the *registry* directory.

Once Vagrant has finished building the VM you can then log on to the VM to start a test;

```vagrant ssh tshoot```

## Starting a test

To start the test;

```
cd /vagrant
os/start_test steve test1 enp0s8
```

Where;
* steve
  - Is the username that will be taking the test
* test1
  - The test to take which is also the name of the Docker image
* enp0s8
  - The real network interface of the system (for VirtualBox this is enp0s8)
  - This is the interface that can be used to ssh to the real server, but the students will be given the port number to use

The username and test name are used by the other scripts to identify which set of Docker containers to manage.

## Performing the test

On completion of the starting of the test the relevant port numbers and IP addresses are displayed which can be used to tell the student how to SSH to the container running the application.  For example if the VM is running on 192.168.1.32, and the SSH port is 1030;

```
ssh -p 1030 student@192.168.1.32
```

The student password for the container is *secret*.

## Completing the test

When the user has completed the test they will run the following command to see if they have completed the test;

```
sudo completed
```

The command will tell them if they have completed the test, or if they need to continue.  If they have already completed the test the system will tell them all the time the container is running.

## Pausing the test

The test can be suspended using;

```/vagrant/os/stop_test steve test1```

## Resuming the test

A test can be resumed at any time with;

```/vagrant/os/resume_test steve test1```

## Destroying the test

Once a test is complete it no longer needs to remain on the system and can be destroyed;

```/vagrant/os/destroy_test steve test1```


# Obtaining the test results

On the Vagrant VM (```vagrant ssh tshoot```) you will be able to see any tests that have been taken.

The /var/testresults directory should be used to find any data on the test the user has taken.

The format of the directory is;

*username*.*test*

In that directory the following are created;
* testcount
  - Contains the number of times the user has ran the *completed* script to check their result
  - Contains the work Completed when the user has finished the test
* logs
  - This directory contains hidden files of;
    .*hostname*_*yyymmdd-HMS*_shell.log
