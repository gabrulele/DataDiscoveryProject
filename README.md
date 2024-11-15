# Data Discovery: Sistema di Ricerca per Documenti Scientifici
Questo progetto implementa un sistema di data discovery per ricercare risultati scientifici all'interno di un corpus di documenti. Utilizza un approccio basato su JSON per l'indicizzazione delle tabelle contenute nei documenti e fornisce un'interfaccia per cercare e recuperare i risultati più pertinenti.

**URL del Progetto**
[https://github.com/gabrulele/DataDiscoveryProject]

## Descrizione
Il sistema permette di cercare all'interno di un corpus di documenti scientifici in formato JSON. Ogni documento può contenere una o più tabelle, e l'utente può effettuare query per cercare risultati che descrivono metriche e proprietà specifiche di un risultato scientifico.

Le tabelle sono già state estratte dai documenti e sono organizzate in un repository JSON. Ogni tabella contiene informazioni come il titolo, il contenuto e metadati correlati come didascalie e paragrafi che citano la tabella. L'obiettivo del sistema è di rendere la ricerca delle tabelle scientifiche più facile ed efficiente, permettendo di eseguire query dettagliate basate sul contenuto delle tabelle.

## Funzionalità
*Indicizzazione*: Le tabelle dei documenti scientifici vengono estratte e organizzate in un formato JSON che viene utilizzato per l'indicizzazione. Ogni tabella è associata a metadati come titolo, contenuto e altre informazioni pertinenti.

*Query*: Gli utenti possono eseguire query per cercare tabelle che corrispondono a determinati criteri. Le query possono essere composte da termini che descrivono proprietà di una tabella, come metriche, dataset e metodi scientifici.

*Risultati Ordinati*: Le tabelle corrispondenti alla query vengono restituite in ordine di pertinenza.

## Struttura del Progetto
Il progetto è organizzato in più moduli:

*Main*: Il punto di ingresso dell'applicazione, dove vengono letti i file JSON, eseguite le query e mostrati i risultati.

*JsonTableParser*: Una classe dedicata all'estrazione e al parsing dei dati da file JSON.

*TableData*: Classe che rappresenta una tabella, contenente i suoi metadati, titolo e contenuto.