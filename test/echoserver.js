
express = require('express')

app = express()

app.post('*', (req, res) => {

    //res.send(req.body + '')

	req.pipe(res)
})

app.listen(5001)





request = require('request')

setTimeout(test, 1000)

function test() {

    request({
        method: 'post',
        url: 'http://localhost:9995',
	headers: {
		'X-Biocad-TargetURL': 'http://localhost:5001',
		'X-Biocad-TargetWants': 'PART_FASTA_PROTEIN',
		'X-Biocad-TargetProvides': 'PART_FASTA_PROTEIN',
		'X-Biocad-ClientWants': 'PART_SBOL',
	},
        body: 'mkvkmk'
    }, function(err, res, body) {

        console.log(err, res, body)

    })
}



