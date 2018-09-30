
It's unrealistic to expect every bioinformatics Web service to accept every possible data standard, particularly if you want the services to be minimal microservices that do one job and do it well.

ConversionProxy is a reverse proxy for services that work with biological "parts", where part could mean a FASTA sequence, a GenBank file, or an [SBOL](http://sbolstandard.org) representation of a synthetic design.

It's designed to sit behind another reverse proxy, such as nginx. The outer proxy is then able to set some specific headers which indicate what ConversionProxy is expected to do:

* `X-Biocad-TargetURL`: The outer proxy forwards to ConversionProxy. This header gives the URL of the next stop (i.e. the inner service)
* `X-Biocad-TargetWants`: The format required by the inner service. For example, if the inner service expects FASTA protein sequences, this could be `PART_FASTA_PROTEIN`.
* `X-Biocad-TargetProvides`: The output format of the inner service. For example, if the inner service outputs an SBOL file, this could be `PART_SBOL`.
* `X-Biocad-ClientWants`: The format expected by the client. For example, if the client expects to receive am SBOL file, this could be `PART_SBOL`.

The outer proxy can infer the values for these headers from the URL.  For example, here's an excerpt from an nginx config that proxies requests to a service providing protein statistics:

    location /emboss/pepstats {

            proxy_http_version 1.1;

            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header Host $http_host;
            proxy_set_header X-NginX-Proxy true;
            proxy_set_header Upgrade $http_upgrade;
            proxy_set_header Connection "upgrade";

            proxy_set_header X-Biocad-TargetWants "PART_FASTA_PROTEIN";
            proxy_set_header X-Biocad-TargetURL "http://127.0.0.1:9996/pepstats";

            proxy_pass http://127.0.0.1:9995/;
            proxy_redirect off;

    }

Now, a client can post FASTA, GenBank, or SBOL, and the inner pepstats service only has to care about processing FASTA.  Notice that the ClientWants and TargetProvides headers were omitted in this case.  That's because we trust the inner service!  If the inner service is smart and can use Accept headers etc. to work out what to send back, that's fine.




