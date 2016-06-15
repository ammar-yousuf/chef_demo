#!/bin/bash
echo "### Running JMeter performance test ###"

# Clear out old results
rm $WORKSPACE/build/reports/jmeter_results.jtl

# Run the tests
echo "## Running the jmeter perf tests on jenkins server"
cd "$WORKSPACE/src/test/resources"

jmeter -n -t ./ezrx_test_plan.jmx -l $WORKSPACE/build/reports/jmeter_results.jtl






