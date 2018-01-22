Vagrant.configure("2") do | config |
  config.vm.define :tshoot do | tshoot |
    config.vm.provider "VirtualBox" do | vb |
      vb.memory = 2048
      vb.name = "tshoot"
      vb.gui = false
    end
    tshoot.vm.host_name = "tshoot.local"
    tshoot.vm.provision "shell", path: "vagrant/shell/provision.sh"
    tshoot.vm.network "public_network"
  end
  config.vm.box="bento/centos-7.2"
end
