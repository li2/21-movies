/*
 * Created by Weiyi Li on 15/03/20.
 * https://github.com/li2
 */
@file:Suppress("unused")

package me.li2.movies.util

import android.view.View
import com.google.android.material.textfield.TextInputLayout
import io.reactivex.rxjava3.android.MainThreadDisposable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import me.li2.android.common.rx.internal.checkMainThread

/**
 * Create an observable which emits on `view` click events.
 */
//fun TextInputLayout.endIconClicks(): Observable<Unit> {
//    return TextInputLayoutEndIconClickObservable(this)
//}

private class TextInputLayoutEndIconClickObservable(
        private val textInputLayout: TextInputLayout
) : Observable<Unit>() {

    override fun subscribeActual(observer: Observer<in Unit>) {
        if (!checkMainThread(observer)) {
            return
        }
        val listener = Listener(textInputLayout, observer)
        observer.onSubscribe(listener)
        textInputLayout.setOnClickListener(listener)
    }

    private class Listener(
            private val textInputLayout: TextInputLayout,
            private val observer: Observer<in Unit>
    ) : MainThreadDisposable(), View.OnClickListener {

        override fun onClick(view: View?) {
            if (!isDisposed) {
                observer.onNext(Unit)
            }
        }

        override fun onDispose() {
            textInputLayout.setEndIconOnClickListener(this)
        }
    }
}
