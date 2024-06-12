package com.example.compose_list_section

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose_list_section.ui.theme.ComposelistsectionTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

sealed class TestSection(val name: String)
{

  class SectionA(name: String, val items: List<SaleItem>) : TestSection(name)

  class SectionB(name: String, val items: List<SaleItem>) : TestSection(name)

  class SectionC(name: String, val items: List<SaleItem>) : TestSection(name)

  class SectionD(name: String, val items: List<SaleItem>) : TestSection(name)

  class SaleItem(val itemName: String)

}

fun getList(): List<TestSection>
{
  val sectionA = "Section A"
  val sectionB = "Section B"
  val sectionC = "Section C"
  val sectionD = "Section D"

  val testSection = mutableListOf(
    TestSection.SectionA(
      sectionA,
      getItems(sectionA)
    ),
    TestSection.SectionB(
      sectionB,
      getItems(sectionB)
    ),
    TestSection.SectionC(
      sectionC,
      getItems(sectionC)
    ),
    TestSection.SectionD(
      sectionD,
      getItems(sectionD)
    )
  )

  return testSection

}

fun getItems(section: String): List<TestSection.SaleItem>
{
  val list = mutableListOf<TestSection.SaleItem>()

  for (index in 1..10)
  {
    list.add(
      TestSection.SaleItem("$section - $index")
    )
  }
  return list
}

class MainActivity : ComponentActivity()
{

  var listIndexSection = mutableListOf<Pair<Int, TestSection>>()

  lateinit var listState: LazyListState

  lateinit var coroutineScope: CoroutineScope

  override fun onCreate(savedInstanceState: Bundle?)
  {
    super.onCreate(savedInstanceState)

    setContent {
      ComposelistsectionTheme {
        Box {
          listState = rememberLazyListState()
          coroutineScope = rememberCoroutineScope()

          sectionElevator(getFlattenedList())
          showList(getFlattenedList())
        }
      }

    }
  }

  @Composable
  private fun showList(list: List<Any>)
  {
    val visibleItems = remember { mutableStateListOf<Any>() }
    val firstVisibleItemIndex by remember {
      derivedStateOf { listState.firstVisibleItemIndex }
    }

    LaunchedEffect(listState) {
      snapshotFlow { listState.isScrollInProgress }.collect { isScrolling ->
        if (isScrolling == false)
        {
          coroutineScope.launch {
            delay(200)

            snapshotFlow { listState.layoutInfo.visibleItemsInfo.map { it.index } }
              .collect { visibleItemIndices ->
                Log.d(
                  "VisibleItems",
                  "Visible items indices: ${
                    visibleItemIndices.map {
                      val item = getFlattenedList()[it]
                      when (item)
                      {
                        is TestSection ->
                        {
                          "${item.name}\n"
                        }

                        is TestSection.SaleItem ->
                        {
                          "${item.itemName}\n"
                        }

                        else -> "---"
                      }
                    }
                  }"
                )
                Log.d("VisibleItems", "Number of visible items: ${visibleItemIndices.size}")
              }
            //            val visibleItemCount = listState.layoutInfo.visibleItemsInfo.size
            //
            //            visibleItems.clear()
            //
            //            val showedList = getFlattenedList().subList(
            //              firstVisibleItemIndex,
            //              (firstVisibleItemIndex+visibleItemCount)//.coerceAtMost(getFlattenedList().size)
            //            )
            //
            //            val showedSaleItems = showedList.toMutableList().filterIsInstance<TestSection.SaleItem>()
            //
            //            visibleItems.addAll(showedSaleItems)
          }
        }
      }
    }

    LazyColumn(
      state = listState!!,
      modifier = Modifier
        .fillMaxSize()
        .padding(0.dp, 50.dp, 0.dp, 0.dp)
    ) {
      itemsIndexed(list) { index, section ->
        // Replace with your item content
        when (section)
        {
          is TestSection.SectionA ->
          {
            listIndexSection.add(Pair(index, section))

            SectionTitle(title = section.name)

            section.items.forEach {
              SectionItemNameVertical(itemName = it.itemName)
            }

          }

          is TestSection.SectionB ->
          {

            listIndexSection.add(Pair(index, section))

            SectionTitle(title = section.name)

            section.items.forEach {
              SectionItemNameVertical(itemName = it.itemName)
            }
          }

          is TestSection.SectionC ->
          {

            listIndexSection.add(Pair(index, section))

            SectionTitle(title = section.name)

            section.items.forEach {
              SectionItemNameVertical(itemName = it.itemName)
            }
          }

          is TestSection.SectionD ->
          {

            listIndexSection.add(Pair(index, section))

            SectionTitle(title = section.name)

            section.items.forEach {
              SectionItemNameVertical(itemName = it.itemName)
            }
          }
        }
      }
    }

    Log.d("MainActivity",
      "Visible items: ${
        visibleItems.joinToString { anyItem ->
          val name = when (anyItem)
          {
            is TestSection.SectionA -> (anyItem as TestSection).name
            is TestSection.SectionB -> (anyItem as TestSection).name
            is TestSection.SectionC -> (anyItem as TestSection).name
            is TestSection.SectionD -> (anyItem as TestSection).name
            is TestSection.SaleItem -> (anyItem as TestSection.SaleItem).itemName
            else                    ->
            {
            }
          }
          "\n $name"
        }
      }")
  }

  @Composable
  fun SectionTitle(title: String)
  {
    Text(
      modifier = Modifier
        .fillMaxSize()
        .padding(0.dp, 0.dp, 0.dp, 0.dp)
        .size(20.dp),
      text = title
    )
  }

  @Composable
  fun SectionItemNameVertical(itemName: String)
  {
    Text(
      modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp),
      text = itemName,
      color = Color.Red,
    )
  }

  @Composable
  fun SectionItemHorizontal(items: List<TestSection.SaleItem>)
  {
    LazyRow {
      items(items.size) { item ->
        Text(
          text = items[item].itemName
        )
      }
    }
  }

  @Composable
  fun sectionElevator(list: List<Any>)
  {
    LazyRow(
      modifier = Modifier
        .padding(0.dp, 10.dp, 0.dp, 0.dp)
        .background(Color.Gray)
    ) {
      items(list.size) {
        if (list[it] is TestSection)
        {
          FilledButtonExample(
            text = (list[it] as TestSection).name,
            onClick = { itemName ->
              coroutineScope?.launch {
                // Animate scroll to the 10th item
                val selectedIndex = listIndexSection.first { it.second.name == itemName }
                listState?.animateScrollToItem(index = selectedIndex.first)
              }
            }
          )

        }
      }
    }
  }

  @Composable
  fun FilledButtonExample(onClick: (String) -> Unit, text: String)
  {
    Button(onClick = { onClick(text) }) {
      Text(text)
    }
  }

  @Composable
  fun Greeting(name: String, modifier: Modifier = Modifier)
  {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .wrapContentSize()
    ) {
      Text(
        text = "Hello $name!",
        modifier = modifier
      )
    }
  }

  @Preview(showBackground = true)
  @Composable
  fun GreetingPreview()
  {
    ComposelistsectionTheme {
      Greeting("Android")
    }
  }

  fun getFlattenedList(): List<Any>
  {
    val sections = getList()
    val flattenedList = mutableListOf<Any>()

    for (section in sections)
    {
      when (section)
      {
        is TestSection.SectionA ->
        {
          flattenedList.add(section)
          flattenedList.addAll(section.items)
        }

        is TestSection.SectionB ->
        {
          flattenedList.add(section)
          flattenedList.addAll(section.items)
        }

        is TestSection.SectionC ->
        {
          flattenedList.add(section)
          flattenedList.addAll(section.items)
        }

        is TestSection.SectionD ->
        {
          flattenedList.add(section)
          flattenedList.addAll(section.items)
        }
      }
    }

    return flattenedList
  }


}

