mongoimport -d buses -c stops --drop stops.csv --type csv --headerline
mongoimport -d buses -c routes --drop routes.csv --type csv --headerline

