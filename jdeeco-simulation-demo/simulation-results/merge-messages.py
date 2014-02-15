import re
from optparse import OptionParser

parser = OptionParser()
parser.add_option("-d", "--directories", dest="dirs",
                  help="Directories to search in")
parser.add_option("-f", "--file", dest="file",
                  help="File name to merge")
parser.add_option("-r", "--results", dest="results",
                  help="Where to store results")
(options, args) = parser.parse_args()

with open(options.results, 'w') as outfile:
    for dir in options.dirs.split():
        with open(dir + "/" + options.file) as infile:
            lines = infile.readlines()
            if len(lines) == 2:
                outfile.write(dir + "      " + lines[1])
                
print "Merging Done"