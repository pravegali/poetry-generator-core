# poetry-generator-core
Core library for generating poetry from a CFG

Work done:
- Spend some time understanding the problem and whiteboarding possible solutions
- Set up a Git repository

Infrastructure:
- Generate a maven project using the quickstart archetype: org.apache.maven.archetypes:maven-archetype-quickstart
- Create a Vagrant box based off a CentOS7 gold image, configure DHCP lease and DNS settings
- Install Apache Tomcat on CentOS7, configure firewall to allow traffic on port 8080
- Create an A record on antiri.com to point poetry.lab.antiri.com to nginx running on my home server
- Configure nginx to route all traffic coming in from poetry.lab.antiri.com por 80 to the newly created vagrant box on port 8080
