package com.example.streetpotholefinder.dataList

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.streetpotholefinder.R
import com.example.streetpotholefinder.R.id.data_list_view
import com.example.streetpotholefinder.RecResultActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.datetime.LocalDateTime
import java.lang.Float.min

class DataListActivity : AppCompatActivity() {
    private val TAG = this.javaClass.simpleName
    lateinit var adapter: DataListAdapter
    var ContentList = mutableListOf<DataListVO>()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_list)

        val rv = findViewById<RecyclerView>(data_list_view)

        var auth = FirebaseAuth.getInstance()
        var fireStore = Firebase.firestore.collection(auth.currentUser?.displayName ?: "devmode")
        fireStore.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    for (tmp_doc in document.documents) {
//                        fireStore.document(tmp_doc.id).collection("porthole").get().addOnSuccessListener {
//                            Log.d(TAG, "document: " + it.documents.size)
//                        }
                        Log.d(TAG, "onCreate: " + tmp_doc.data)

                        var _date : String = ""
                        var _time : String = ""
                        var _durate : String = ""
                        var _cntPothole : Long = 0
                        var _cntCrack : Long = 0

                        tmp_doc.data!!.forEach{ (key, value) ->
                            when(key){
                                "recStartTime" -> {
                                    _date = LocalDateTime.parse(value as String).date.toString()
                                    _time = LocalDateTime.parse(value as String).time.toString()
                                }
                                "cntCrack" -> {
                                    _cntCrack = value as Long
                                }
                                "cntPothole" -> {
                                    _cntPothole = value as Long
                                }
                            }
                        }
                        ContentList.add(DataListVO(_date,_time,"0",_cntPothole.toString(),_cntCrack.toString()))
                        Log.d(TAG, "onCreate: "+_date+_time+"0"+_cntPothole.toString()+_cntCrack.toString())
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }

        adapter = DataListAdapter(ContentList)

        rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv.setHasFixedSize(true)

        adapter = DataListAdapter(ContentList)

        rv.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )

        var swipeHelperCallback = SwipeHelperCallback().apply { setClamp(250f) }
        val itemTouchHelper = ItemTouchHelper(swipeHelperCallback)
        itemTouchHelper.attachToRecyclerView(rv)

        rv.setOnTouchListener { _, _ ->
            swipeHelperCallback.removePreviousClamp(rv)
            false
        }

        rv.adapter = adapter

    }

    //데이터목록 각 리스트 클릭 시 이벤트
//        rv.onItemClickListener = AdapterView.OnItemClickListener{
//                parent, view, position, id->
//            val selection = parent.getItemAtPosition(position) as DataListVO
//            //토스트로 작동하는지 확인
//            Toast.makeText(this,"${selection.StreetDate}", Toast.LENGTH_SHORT).show()
//        }

}


class DataListAdapter(val dataList: MutableList<DataListVO>) :
    RecyclerView.Adapter<DataListAdapter.CustomViewHolder>() {
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {

        context = parent.context
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.data_list_one_lyt, parent, false)
        return CustomViewHolder(view).apply {
            llyt_swipe_view.setOnClickListener {

                var curpos: Int = adapterPosition
                var intent = Intent(context, RecResultActivity::class.java)

                intent.putExtra("previousActivityInfo", "DataListAdapter")
                intent.putExtra("number", curpos)
                context.startActivity(intent)
            }
        }
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.RecordLength.text = dataList.get(position).RecordLength
        holder.StreetDate.text = dataList.get(position).StreetDate
        holder.StreetTime.text = dataList.get(position).StreetTime
        holder.PotholeCnt.text = dataList.get(position).PotholeCnt
        holder.CrackCnt.text = dataList.get(position).CrackCnt

        holder.onDeleteClick = {
            removeItem(it)
        }
    }

    fun removeItem(viewHolder: RecyclerView.ViewHolder) {
        var position = viewHolder.adapterPosition
        dataList.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val StreetDate = itemView.findViewById<TextView>(R.id.StreetDate)
        val StreetTime = itemView.findViewById<TextView>(R.id.StreetTime)
        val RecordLength = itemView.findViewById<TextView>(R.id.RecordLength)
        val PotholeCnt = itemView.findViewById<TextView>(R.id.PotholeCnt)
        val CrackCnt = itemView.findViewById<TextView>(R.id.CrackCnt)
        val btndel = itemView.findViewById<ImageView>(R.id.btndel)
        val llyt_swipe_view = itemView.findViewById<LinearLayout>(R.id.llyt_swipe_view)

        var onDeleteClick: ((RecyclerView.ViewHolder) -> Unit)? = null

        init {
            btndel.setOnClickListener {
                onDeleteClick?.let { onDeleteClick ->
                    onDeleteClick(this)
                }
            }
        }
    }
}

class DataListVO(
    val StreetDate: String,
    val StreetTime: String,
    val RecordLength: String,
    val PotholeCnt: String,
    val CrackCnt: String

) {
    override fun toString(): String {
        return "DataListVO(StreetDate='$StreetDate', StreetTime='$StreetTime', RecordLength='$RecordLength', PotholeCnt='$PotholeCnt', CrackCnt='$CrackCnt')"
    }
}

class SwipeHelperCallback : ItemTouchHelper.Callback() {

    private var currentPosition: Int? = null
    private var previousPosition: Int? = null
    private var currentDx = 0f
    private var clamp = 0f

    override fun getMovementFlags(
        recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(0, ItemTouchHelper.LEFT)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        currentDx = 0f
        getDefaultUIUtil().clearView(getView(viewHolder))
        previousPosition = viewHolder.adapterPosition
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        viewHolder?.let {
            currentPosition = viewHolder.adapterPosition
            getDefaultUIUtil().onSelected(getView(it))
        }
    }

    // view밖으로 escape되는 것 막기
    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return defaultValue * 10
    }

    // view밖으로 escape되는 것 막기
    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        val isClamped = getTag(viewHolder)
        // 현재 View가 고정되어있지 않고 사용자가 -clamp 이상 swipe시 isClamped true로 변경 아닐시 false로 변경
        setTag(viewHolder, !isClamped && currentDx <= -clamp)
        return 2f
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val view = getView(viewHolder)
            val isClamped = getTag(viewHolder)  //고정할지 말지 결정, true : 고정함  false:고정안함
            val x = clampViewPositionHorizontal(
                view, dX, isClamped, isCurrentlyActive
            )    //x만큼 이동(고정 해제 시 이동위치 결정)

            // 고정시킬 시 애니메이션 추가
            if (x == -clamp) {
                getView(viewHolder).animate().translationX(-clamp).setDuration(100L).start()
                return
            }

            currentDx = x
            getDefaultUIUtil().onDraw(
                c, recyclerView, view, dX, dY, actionState, isCurrentlyActive
            )
        }
    }


    private fun clampViewPositionHorizontal(
        view: View, dX: Float, isClamped: Boolean, isCurrentlyActive: Boolean
    ): Float {

        // RIGHT 방향으로 swipe 막기
        val max: Float = 0f

        //고정할 수 있으면
        val x = if (isClamped) {
            // View가 고정되었을 때 swipe되는 영역 제한
            if (isCurrentlyActive) dX - clamp else -clamp
        } else {
            // 고정할 수 없으면 x는 스와이프한 만큼
            dX / 2
        }
        //return min(max(min,x),max)
        return min(x, max)
    }

    private fun setTag(viewHolder: RecyclerView.ViewHolder, isClamped: Boolean) {
        // isClamped를 view의 tag로 관리
        viewHolder.itemView.tag = isClamped
    }

    private fun getTag(viewHolder: RecyclerView.ViewHolder): Boolean {
        // isClamped를 view의 tag로 관리
        return viewHolder.itemView.tag as? Boolean ?: false
    }

    private fun getView(viewHolder: RecyclerView.ViewHolder): View {
        return (viewHolder as DataListAdapter.CustomViewHolder).itemView.findViewById(R.id.llyt_swipe_view)
    }

    fun setClamp(clamp: Float) {
        this.clamp = clamp
    }

    // 다른 View가 swipe 되거나 터치되면 고정 해제
    fun removePreviousClamp(recyclerView: RecyclerView) {
        if (currentPosition == previousPosition) return
        previousPosition?.let {
            val viewHolder = recyclerView.findViewHolderForAdapterPosition(it) ?: return
            getView(viewHolder).translationX = 0f
            setTag(viewHolder, false)
            previousPosition = null
        }
    }


}




