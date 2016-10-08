# Starting demo processing clusters.

If not already installed, install ec2-cli-tools and ansible on the host you are running these commands on.

    sudo apt-get install ec2-api-tools ansible

Run tools from the ansible directory.

    cd ansible

Make sure that your SSH agent has the proper identities loaded, also make sure that you have your `AWS_ACCESS_KEY` and `AWS_SECRET_KEY` env variables set correctly.  (startInstances.sh can be run from anywhere the ec2-cli-api tools are properly configured.)

    ssh-add -l # verify ssh keys.  For this configuration, you should have int.pem loaded.
    export AWS_ACCESS_KEY=YOUR_AWS_ACCESS_KEY
    export AWS_SECRET_KEY=YOUR_AWS_SECRET_KEY
    scripts/startInstances.sh -n clusterName -c instanceCount

After the instances are running and ready, edit the clusterName.hosts.XXXXX file and insert [cm] on the second to last line, should look as follows:  

    ip-10-101-x-a.indatasights.int
    ip-10-101-x-b.indatasights.int
    [cm]
    ip-10-101-x-c.indatasights.int

Then run the hCluster.yml playbook to install java and configure kernels on all machines.

    ansible-playbook -i clusterName.hosts.XXXXX hCluster.yml

Then run  the clouderaManager playbook to install CM on the designated CM server.

    ansible-playbook -i clusterName.hosts.XXXXX clouderaManager.yml

Use the output of the following command to generate a list of hosts that will be fed to CM.

    grep ^ip clusterName.hosts.XXXXX

The rest of the cluster configuration happens through Cloudera Manager, which will be covered in a different document.

    http://ip-10-101-x-c.indatasights.int:7180

Termination of cluster.  Make sure to copy any data off of the cluster before running the  following.  

    ec2-terminate-instances $(cat clusterName.instances.XXXXX)



### Notes

The performance of instantiating clusters could be increased by creating our own software repositories, much time is spent downloading parcels.   

Copy namenode:/etc/hadoop/conf.cloudera.yarn to your local machine, run hadoop commands with --config that points to new cluster config. 

    hadoop --config conf.cloudera.yarn

As of 20150930, all of this worked.
