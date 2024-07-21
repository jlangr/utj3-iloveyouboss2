The QuestionController requires a Persistence provider.

Jazzer:

"""
Assuming your test class is called com.example.MyFuzzTests, create the inputs directory src/test/resources/com/example/MyFuzzTestsInputs.
Run a fuzz test with the environment variable JAZZER_FUZZ set to 1 to let the fuzzer rapidly try new sets of arguments.
Run the fuzz test without JAZZER_FUZZ set to execute it only on the inputs in the inputs directory. This mode, which behaves just like a traditional unit test, ensures that issues previously found by the fuzzer remain fixed and can also be used to debug the fuzz test on individual inputs.


Creating objects from fuzzer input can lead to many reported exceptions. Jazzer addresses this issue by ignoring exceptions that the target method declares to throw.
"""
