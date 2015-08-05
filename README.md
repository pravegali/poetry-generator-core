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

Requirements:
The design and programming exercise is to create a random poem generator using the following grammatical rules:

    POEM: <LINE> <LINE> <LINE> <LINE> <LINE>
    LINE: <NOUN>|<PREPOSITION>|<PRONOUN> $LINEBREAK
    ADJECTIVE: black|white|dark|light|bright|murky|muddy|clear <NOUN>|<ADJECTIVE>|$END
    NOUN: heart|sun|moon|thunder|fire|time|wind|sea|river|flavor|wave|willow|rain|tree|flower|field|meadow|pasture|harvest|wate father|mother|brother|sister <VERB>|<PREPOSITION>|$END
    PRONOUN: my|your|his|her <NOUN>|<ADJECTIVE>
    VERB: runs|walks|stands|climbs|crawls|flows|flies|transcends|ascends|descends|sinks <PREPOSITION>|<PRONOUN>|$END
    REPOSITION: above|across|against|along|among|around|before|behind|beneath|beside|between|beyond|during|inside|onto|outside|under|underneath|upon|with|without|through <NOUN>|<PRONOUN>|<ADJECTIVE>

- To the left of the colon is the name of the rule
- To the right of the colon is the rule definition which can consist of words, keywords and references to other rules.
- A reference to another rule is marked with angle brackets, for example <NOUN>. Rules can reference themselves, making them recursive.
- Keywords are marked with $. There are two keywords: LINEBREAK and END. LINEBREAK adds a line break to the output, END marks that the end of a line has been reached. This means that a line can only end with an adjective, a noun or a verb.
- A grouping of elements separated by | means that one of those elements should be selected at random.
- Anything else that is plain text can be considered a word, for example  murky or  heart.
- For example, the rule PRONOUN is defined as my|your|his|her <NOUN>|<ADJECTIVE> which means that one of the words my, your, his or her should be selected at random followed by either a NOUN or an ADJECTIVE, also selected at random.

Write a Java or Scala program which parses the grammatical rules from a text file, then uses the parsed data to generate a random poem. Here is an example of what the output might look like:

    my sun among her white meadow
    moon upon my light
    moon
    your rain climbs
    her murky bright clear willow
