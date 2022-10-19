package com.example.rxkotlindemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.rxkotlindemo.databinding.FragmentCreateOperatorsBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class CreateOperatorsFragment : Fragment() {

    private lateinit var binding: FragmentCreateOperatorsBinding

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCreateOperatorsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding()
    }

    private fun viewBinding() {
        binding.btnJust.setOnClickListener {
            onHandleJust()
        }

        binding.btnCreate.setOnClickListener {
            onHandleCreate()
        }

        binding.btnRange.setOnClickListener {
            onHandleRange()
        }
        binding.btnRepeat.setOnClickListener {
            onHandleRepeat()
        }
        binding.btnInterval.setOnClickListener {
            onHandleInterval()
        }
        binding.btnTimer.setOnClickListener {
            onHandleTimer()
        }
        binding.btnFromArray.setOnClickListener {
            onHandleFromArray()
        }
        binding.btnFromIterable.setOnClickListener {
            onHandleFromIterable()
        }
    }

    private fun onHandleJust(){
        binding.tvOutput.text = ""
        binding.tvOperationType.text = getString(R.string.just)
        val inputString = "Observed events from 1 to 3"
        binding.tvInput.text = inputString
        val disposable = Observable.just(1, 2, 3)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                binding.tvOutput.append("$result ") // onNext
            },{error ->
                binding.tvOutput.text = error.message
            })
        compositeDisposable.add(disposable)
    }

    private fun onHandleCreate(){
        binding.tvOutput.text = ""
        binding.tvOperationType.text = getString(R.string.create)
        val inputString = "Observed events \"Ukraine\", \"\", \"Poland\""
        binding.tvInput.text = inputString
        val list = listOf("Ukraine", "", "Poland")
        val disposable = getObservableFromList(list)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                binding.tvOutput.append("$result ") // onNext
            },{error ->
                binding.tvOutput.append(error.message)
            }
            )
        compositeDisposable.add(disposable)
    }

    private fun getObservableFromList(myList: List<String>): Observable<String>{
          return Observable.create{
              emitter->
              myList.forEach{
                  if (it.isBlank()) {
                      emitter.onError(Exception("Error: item cannot be blank"))
                  }
                  emitter.onNext(it)
              }
          }
    }

    private fun onHandleRepeat() {
        binding.tvOutput.text = ""
        binding.tvOperationType.text = getString(R.string.repeat)
        val inputString = "Observed events \"Ukraine\", \"USA\", \"Poland\" - .repeat(2)"
        binding.tvInput.text = inputString
        val array = listOf("Ukraine", "USA", "Poland")
        val disposable = Observable.fromArray(array)
            .repeat(2)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                binding.tvOutput.append("$result ") // onNext
            }, { error ->
                binding.tvOutput.append(error.message)
            })
        compositeDisposable.add(disposable)
    }

    private fun onHandleRange() {
        binding.tvOutput.text = ""
        binding.tvOperationType.text = getString(R.string.range)
        val inputString = "Observable.rangeLong(30, 5)"
        binding.tvInput.text = inputString
        val disposable = Observable.rangeLong(30, 5).subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                binding.tvOutput.append("$result ") // onNext
            }, { error ->
                binding.tvOutput.append(error.message)
            })
        compositeDisposable.add(disposable)
    }

    private fun onHandleInterval() {
        binding.tvOutput.text = ""
        binding.tvOperationType.text = getString(R.string.interval)
        val inputString = "Observable.intervalRange(\n" +
                "1,// Start\n" +
                "7,// Count\n" +
                "0,// Initial Delay\n" +
                "1,// Period between events\n" +
                "TimeUnit.SECONDS)"
        binding.tvInput.text = inputString
        val disposable = Observable.intervalRange(
            1,     // Start
            7,      // Count
            0,      // Initial Delay
            1,      // Period between events
            TimeUnit.SECONDS
        )
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                binding.tvOutput.append("$result ") // onNext
            }, { error ->
                binding.tvOutput.append(error.message)
            })

        compositeDisposable.add(disposable)
    }

    private fun onHandleTimer() {
        binding.tvOutput.text = ""
        binding.tvOperationType.text = getString(R.string.timer)
        val inputString = "Observable.timer(\n" +
                "3L,// Delay\n" +
                "TimeUnit.SECONDS)"
        binding.tvInput.text = inputString
        val disposable = Observable.timer(
            3L,// Delay
            TimeUnit.SECONDS
        )
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                binding.tvOutput.append("$result ") // onNext
            }, { error ->
                binding.tvOutput.append(error.message)
            })
        compositeDisposable.add(disposable)
    }

    private fun onHandleFromArray() {
        binding.tvOutput.text = ""
        binding.tvOperationType.text = getString(R.string.from_array)
        val inputString = "val array = arrayOf(\"Ukraine\", \"USA\", \"Poland\"\n)" + "Observable.fromArray(*array)"
        binding.tvInput.text = inputString
        val array = arrayOf("Ukraine", "USA", "Poland")
        val disposable = Observable.fromArray(*array)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                binding.tvOutput.append("$result ")
            }, { error ->
                binding.tvOutput.append(error.message)
            })
        compositeDisposable.add(disposable)
    }

    private fun onHandleFromIterable() {
        binding.tvOutput.text = ""
        binding.tvOperationType.text = getString(R.string.from_iterable)
        val inputString = "val list = listOf(\"Ukraine\", \"USA\", \"Poland\"\n)" + "Observable.fromIterable(list)"
        binding.tvInput.text = inputString
        val list = listOf("Ukraine", "USA", "Poland")
        val disposable = Observable.fromIterable(list)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                binding.tvOutput.append("$result ")
            }, { error ->
                binding.tvOutput.append(error.message)
            })
        compositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}